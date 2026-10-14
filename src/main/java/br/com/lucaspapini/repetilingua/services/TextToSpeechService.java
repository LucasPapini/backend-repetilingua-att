package br.com.lucaspapini.repetilingua.services;

import br.com.lucaspapini.repetilingua.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class TextToSpeechService {
    private static final Logger logger = LoggerFactory.getLogger(TextToSpeechService.class);

    @Autowired
    private UserService userService;

    private final Path rootLocation;
    private final RestTemplate restTemplate;

    public TextToSpeechService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        this.restTemplate = new RestTemplate();
    }

    /**
     * Gera o arquivo de áudio MP3 a partir de um texto em inglês e salva no diretório base.
     *
     * @param text Frase em inglês a ser sintetizada.
     * @return Caminho relativo do arquivo salvo (ex: "flashcards/audio_12345.mp3")
     */
    public String generateAudioFromText(String text) {
        logger.info("Gerando áudio via TTS para a frase: '{}'", text);
        User me = userService.authenticated();
        try {
            // 1. Constrói a URL da requisição de TTS (Google Translate TTS API)
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://translate.google.com/translate_tts")
                    .queryParam("ie", "UTF-8")
                    .queryParam("tl", "en") // Sotaque em inglês (en-US / en-GB)
                    .queryParam("client", "tw-ob")
                    .queryParam("q", text)
                    .build()
                    .encode()
                    .toUri();
            logger.info(" Constrói a URL da requisição de TTS (Google Translate TTS API): '{}'", uri);

            // 2. Define o User-Agent para evitar bloqueios 403 HTTP Forbidden
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            logger.info("Define o User-Agent para evitar bloqueios 403 HTTP Forbidden");
            logger.info("headers '{}'", headers);
            logger.info("entity '{}'", entity);

            // 3. Executa a requisição buscando os bytes do áudio MP3
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            logger.info(" Executa a requisição buscando os bytes do áudio MP3: '{}'", response);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 4. Define o diretório de destino usando a variável de diretório base (rootLocation)
                // Ex: uploads/audio/user-1/flashcard
                Path userFlashcardDir = Paths.get("audios", "user-" + me.getId(), "flashcard");

                // Cria os diretórios no disco se ainda não existirem
                if (!Files.exists(userFlashcardDir)) {
                    Files.createDirectories(userFlashcardDir);
                }

                // 5. Normaliza o texto para remover caracteres inválidos em nomes de arquivos e gera o nome
                String textParaSalvarArquivo = text.replaceAll("[^a-zA-Z0-9.-]", "-").toLowerCase();
                String filename =  "user_id-" + me.getId() + "-" + textParaSalvarArquivo + ".mp3";

                // Caminho completo onde o arquivo será gravado no disco
                Path targetFilePath = userFlashcardDir.resolve(filename);

                // 6. Escreve os bytes da resposta diretamente no arquivo
                Files.write(targetFilePath, response.getBody(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                logger.info("Áudio salvo com sucesso em: {}", targetFilePath.toAbsolutePath());

                // 7. Retorna o caminho relativo ajustado para o seu endpoint de streaming (/audios/...)
                // Ex: "audio/user-1/flashcard/flashcard_1_hello-world.mp3"
                return "audio/user-" + me.getId() + "/flashcard/" + filename;
            } else {
                logger.error("Falha ao obter áudio da API de TTS. Status: {}", response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            logger.error("Erro ao comunicar com o serviço de TTS", e);
            return null;
        }
    }
}
