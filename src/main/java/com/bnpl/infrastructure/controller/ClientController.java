package com.bnpl.infrastructure.controller;

import com.bnpl.application.ClientService;
import com.bnpl.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientDTO> registerClient(@RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.registerClient(clientDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }
}
