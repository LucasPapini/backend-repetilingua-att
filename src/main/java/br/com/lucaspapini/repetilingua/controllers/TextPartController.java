package br.com.lucaspapini.repetilingua.controllers;

import br.com.lucaspapini.repetilingua.enums.TextPartProgressEnum;
import br.com.lucaspapini.repetilingua.controllers.docs.TextPartControllerDocs;
import br.com.lucaspapini.repetilingua.entities.dto.*;
import br.com.lucaspapini.repetilingua.services.TextPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping(value = "/texts-parts")
public class TextPartController implements TextPartControllerDocs {
    @Autowired
    private TextPartService service;

    private static final Logger logger = LoggerFactory.getLogger(TextPartController.class);

    @GetMapping
    public ResponseEntity<Page<TextPartListDTO>> findAll(Pageable pageable) {
        logger.info("Executando findAll > TextParts...");
        Page<TextPartListDTO> list = service.findAll(pageable);
        logger.info("Retornando objetos obtidos: {}", list.getContent());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<TextPartResponseDTO> findById(@PathVariable("id") Long id) {
        logger.info("Executando findById > TextParts (ID: {})...", id);
        TextPartResponseDTO dto = service.findById(id);
        logger.info("Retornando objeto obtido: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<TextPartCreateRequestDTO> create(@RequestBody TextPartCreateRequestDTO dto) {
        logger.info("Executando create > TextParts...");
        TextPartCreateRequestDTO result = service.create(dto);
        logger.info("Retornando objeto criado: {}", result);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getTextId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<TextPartUpdateRequestDTO> update(
            @PathVariable("id") Long id,
            @RequestBody TextPartUpdateRequestDTO dto
    ) {
        logger.info("Executando update > TextParts (ID: {})...", id);
        TextPartUpdateRequestDTO result = service.update(id, dto);
        logger.info("Retornando objeto atualizado: {}", result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) {
        logger.info("Executando delete > TextParts (ID: {})...", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/stats")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<Page<TextPartStatsResponseDTO>> getTextPartStat(@PathVariable("id") Long id, Pageable pageable){
        logger.info("Executando getTextPartStat > TextParts...");
        Page<TextPartStatsResponseDTO> dto = service.getTextPartStat(id, pageable);
        logger.info("Retornando objetos: {}", dto.getContent());
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{textId}/part/{partNumber}/study")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<TextPartStudyResponseDTO> getTextPartStudy(@PathVariable("textId") Long textId, @PathVariable("partNumber") Long partNumber){
        logger.info("Executando getTextPartStudy > TextParts (ID: {})...", textId);
        TextPartStudyResponseDTO dto = service.getTextPartStudy(textId, partNumber);
        logger.info("Retornando objetos: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/part/{id}/text/{idText}/progress")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<Void> setProgressTextPart(
            @PathVariable("id") Long idTextPart,
            @PathVariable("idText") Long idText,
            @RequestParam TextPartProgressEnum field
    ){
        logger.info("Executando setProgressTextPart > TextParts (idTextPart: {})...", idTextPart);
        logger.info("Executando setProgressTextPart > TextParts (idText: {})...", idText);
        logger.info("Executando setProgressTextPart > TextParts (field: {})...", field);
        service.setProgressTextPart(idTextPart, idText, field);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("textId") Long textId
    ) throws IOException {
        logger.info("Executando upload > TextParts (textId: {})...", textId);
        String audioPath = service.upload(file, textId);
        return ResponseEntity.ok(audioPath);
    }

}
