package br.com.lucaspapini.repetilingua.controllers;

import br.com.lucaspapini.repetilingua.entities.dto.UserDTO;
import br.com.lucaspapini.repetilingua.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR', 'ROLE_ADMIN')")
    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> getMe(){
        logger.info("Buscando lista de usuários...");
        UserDTO dto = service.getMe();
        logger.info("Retornando úsuario: {}", dto.toString());
        return ResponseEntity.ok(dto);
    }
}
