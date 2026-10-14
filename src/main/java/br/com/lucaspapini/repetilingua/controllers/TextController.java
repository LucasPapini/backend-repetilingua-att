package br.com.lucaspapini.repetilingua.controllers;

import br.com.lucaspapini.repetilingua.controllers.docs.TextControllerDocs;
import br.com.lucaspapini.repetilingua.entities.dto.*;
import br.com.lucaspapini.repetilingua.services.TextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/texts")
public class TextController implements TextControllerDocs {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TextService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<Page<TextListDTO>> findAll(Pageable pageable) {
        logger.info("Executando findAll > Text...");
        Page<TextListDTO> list = service.findAll(pageable);
        logger.info("Retornando objetos obtidos: {}", list.getContent());
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<TextResponseDTO> findById(@PathVariable("id") Long id) {
        logger.info("Executando findById > Text (ID: {})...", id);
        TextResponseDTO dto = service.findById(id);
        logger.info("Retornando objeto obtido: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<TextCreateRequestDTO> create(@RequestBody TextCreateRequestDTO dto) {
        logger.info("Executando create > Text...");
        TextCreateRequestDTO result = service.insert(dto);
        logger.info("Retornando objeto criado: {}", result);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<TextUpdateRequestDTO> update(@PathVariable("id") Long id, @RequestBody TextUpdateRequestDTO dto) {
        logger.info("Executando update > Text (ID: {})...", id);
        TextUpdateRequestDTO result = service.update(id, dto);
        logger.info("Retornando objeto atualizado: {}", result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {
        logger.info("Executando delete > Text (ID: {})...", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/resume")
    public ResponseEntity<Page<TextResumeResponseDTO>> resumeText(Pageable pageable){
        logger.info("Executando resumeText > Text...");
        Page<TextResumeResponseDTO> page = service.findResumeTexts(pageable);
        logger.info("Retornando objetos: {}", page.getContent());
        return ResponseEntity.ok(page);
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}/stats")
    public ResponseEntity<TextStatsResponseDTO> getTextStat(@PathVariable("id") Long id){
        logger.info("Executando getTextStat > Text (ID: {})...", id);
        TextStatsResponseDTO dto = service.getTextStats(id);
        logger.info("Retornando objeto localizado: {}", dto);
        return ResponseEntity.ok(dto);
    }
}
