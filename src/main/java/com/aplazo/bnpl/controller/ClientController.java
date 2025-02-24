package com.aplazo.bnpl.controller;

import com.aplazo.bnpl.dto.ClientDto;
import com.aplazo.bnpl.dto.ClientRequest;
import com.aplazo.bnpl.dto.ClientResponse;
import com.aplazo.bnpl.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientResponse> registerClient(@RequestBody ClientRequest clientRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(clientRequest.getBirthDate(), formatter);

        ClientDto clientDto = clientService.registerClient(clientRequest.getName(), date);

        ClientResponse clientResponse = new ClientResponse().set(clientDto);

        return ResponseEntity.status(201).body(clientResponse);
    }
}
