package com.bnpl.infrastructure.controller;

import com.bnpl.application.ClientService;
import com.bnpl.dto.ClientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerClient_ShouldReturnClientDTO() {
        ClientDTO clientDTO = ClientDTO.builder()
                .id(1L)
                .name("Carlos")
                .birthDate(LocalDate.of(1995, 1, 1))
                .creditLine(5000.0)
                .build();

        when(clientService.registerClient(any(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.registerClient(clientDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5000.0, response.getBody().getCreditLine());
        verify(clientService, times(1)).registerClient(clientDTO);
    }
}
