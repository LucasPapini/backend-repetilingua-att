package br.com.lucaspapini.repetilingua.controllers;

import br.com.lucaspapini.repetilingua.entities.dto.FlashcardCreateRequestDTO;
import br.com.lucaspapini.repetilingua.entities.dto.FlashcardResponseDTO;
import br.com.lucaspapini.repetilingua.services.FlashcardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/flashcards")
public class FlashcardController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private FlashcardService flashcardService;

//    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<FlashcardResponseDTO> create(@RequestBody FlashcardCreateRequestDTO dto){
        logger.info("Executando create > Flashcard...");
        FlashcardResponseDTO response = flashcardService.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
