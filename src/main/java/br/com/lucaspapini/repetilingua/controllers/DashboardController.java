package br.com.lucaspapini.repetilingua.controllers;

import br.com.lucaspapini.repetilingua.entities.dto.StatsDTO;
import br.com.lucaspapini.repetilingua.services.TextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(TextPartController.class);

    @Autowired
    private TextService textService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping(value = "/stats")
    public ResponseEntity<StatsDTO> getStats(){
        logger.info("Executando getStats > Dashboard...");
        StatsDTO dto = textService.getAllStats();
        logger.info("Retornando objetos obtidos: {}", dto.toString());
        return ResponseEntity.ok(dto);
    }
}
