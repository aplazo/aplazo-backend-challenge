package com.aplazo.bnpl.controller;

import com.aplazo.bnpl.dto.ClientDto;
import com.aplazo.bnpl.dto.ClientRequest;
import com.aplazo.bnpl.dto.ClientResponse;
import com.aplazo.bnpl.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRegisterClient() {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("John Doe");
        clientRequest.setBirthDate("1990/01/01");

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setCreditLine(1000.0);

        when(clientService.registerClient(any(String.class), any(LocalDate.class))).thenReturn(clientDto);

        ResponseEntity<ClientResponse> response = clientController.registerClient(clientRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        assertEquals(1000.0, response.getBody().getCreditLine());
    }
}
