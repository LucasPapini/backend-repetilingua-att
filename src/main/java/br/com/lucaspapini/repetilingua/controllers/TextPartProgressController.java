package br.com.lucaspapini.repetilingua.controllers;

import br.com.lucaspapini.repetilingua.controllers.docs.TextPartProgressControllerDocs;
import br.com.lucaspapini.repetilingua.entities.dto.TextPartProgresstCreateDTO;
import br.com.lucaspapini.repetilingua.entities.dto.TextPartProgresstDTO;
import br.com.lucaspapini.repetilingua.services.TextPartProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/progress")
public class TextPartProgressController implements TextPartProgressControllerDocs {
    private static final Logger logger = LoggerFactory.getLogger(TextPartController.class);

    @Autowired
    private TextPartProgressService service;

    @GetMapping
    public ResponseEntity<Page<TextPartProgresstDTO>> findeAll(Pageable pageable){
        logger.info("Executando findAll > TextPartProgress...");
        Page<TextPartProgresstDTO> list = service.findAll(pageable);
        logger.info("Retornando objetos obtidos: {}", list.getContent());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{id}/text-part/{idTextPart}")
    public ResponseEntity<TextPartProgresstDTO> findByIdAndIdPartText(@PathVariable("id") Long id, @PathVariable("idTextPart") Long idTextPart ){
        logger.info("Executando findByIdAndIdPartText > TextPartProgress (ID: {})...", id);
        TextPartProgresstDTO dto = service.findByIdAndByIdTextPart(id, idTextPart);
        logger.info("Retornando objeto obtido: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/text-part/{idTextPart}")
    public ResponseEntity<TextPartProgresstCreateDTO> update(@RequestBody TextPartProgresstCreateDTO dto){
        logger.info("Executando update > TextParts (ID: {})...", dto.getIdTextPart());
        TextPartProgresstCreateDTO result  = service.update(dto);
        logger.info("Retornando objeto atualizado: {}", result);
        return ResponseEntity.ok(result);
    }
}
