package com.aplazo.bnpl.challenge.infrastructure.controller;

import com.aplazo.bnpl.challenge.application.service.ClientServiceImpl;
import com.aplazo.bnpl.challenge.dto.ClientRequest;
import com.aplazo.bnpl.challenge.dto.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientServiceImpl clientService;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientResponse> registerClient(@RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.registerClient(request));
    }

}
