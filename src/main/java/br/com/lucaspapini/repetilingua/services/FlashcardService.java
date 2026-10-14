package br.com.lucaspapini.repetilingua.services;

import br.com.lucaspapini.repetilingua.entities.Deck;
import br.com.lucaspapini.repetilingua.entities.Flashcard;
import br.com.lucaspapini.repetilingua.entities.FlashcardProgress;
import br.com.lucaspapini.repetilingua.entities.User;
import br.com.lucaspapini.repetilingua.entities.dto.FlashcardCreateRequestDTO;
import br.com.lucaspapini.repetilingua.entities.dto.FlashcardResponseDTO;
import br.com.lucaspapini.repetilingua.repositories.DeckRepository;
import br.com.lucaspapini.repetilingua.repositories.FlashcardProgressRepository;
import br.com.lucaspapini.repetilingua.repositories.FlashcardRepository;
import br.com.lucaspapini.repetilingua.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
public class FlashcardService {
    @Autowired
    private FlashcardRepository repository;

    @Autowired
    private FlashcardProgressRepository progressRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private TextToSpeechService ttsService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckRepository deckRepository;

    @Transactional
    public FlashcardResponseDTO create(FlashcardCreateRequestDTO dto){
        User me = userService.authenticated();

        // 1. Gera o áudio da frase em inglês
        String audioRelativePath = ttsService.generateAudioFromText(dto.frontText());

        // 2. Monta o link final acessível no seu controller de áudio existente
        String fullAudioUrl = audioRelativePath != null ? "/audios/" + audioRelativePath : null;

        // Busca Deck para reclacionar a frase
        Deck deck = deckRepository.findById(dto.deckId())
                .orElseThrow(() -> new ResourceNotFoundException("Deck não encontrado"));

        // 3. Salva o Flashcard
        Flashcard flashcard = Flashcard.builder()
                .frontText(dto.frontText())
                .backText(dto.backText())
                .audioUrl(fullAudioUrl)
                .deck(deck)
                .createdAt(Instant.now())
                .build();

        flashcard = flashcardRepository.save(flashcard);

         // 4. Inicializa o progresso SRS do usuário para esta frase
        FlashcardProgress progress = FlashcardProgress.builder()
                .userId(me.getId())
                .flashcard(flashcard)
                .intervalDays(0)
                .easeFactor(2.5)
                .repetitions(0)
                .nextReviewDate(LocalDate.now())
                .build();

        progressRepository.save(progress);

        return FlashcardResponseDTO.fromEntity(flashcard);

    }
}
