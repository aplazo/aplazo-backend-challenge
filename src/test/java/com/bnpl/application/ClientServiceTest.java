package com.bnpl.application;

import com.bnpl.domain.Client;
import com.bnpl.dto.ClientDTO;
import com.bnpl.infrastructure.repository.ClientRepository;
import com.bnpl.mapper.ClientMapper;
import com.bnpl.strategy.CreditLineStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bnpl.exceptions.ClientNotFoundException;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private CreditLineStrategy creditLineStrategy;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerClientSuccess() {
        ClientDTO clientDTO = ClientDTO.builder()
                .name("Carlos")
                .birthDate(LocalDate.of(1995, 1, 1))
                .build();

        Client client = Client.builder()
                .id(1L)
                .name("Carlos")
                .birthDate(clientDTO.getBirthDate())
                .creditLine(5000.0)
                .build();

        when(creditLineStrategy.assignCreditLine(anyInt())).thenReturn(5000.0);
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.toDTO(any(Client.class))).thenReturn(clientDTO);

        ClientDTO result = clientService.registerClient(clientDTO);

        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        verify(clientRepository, times(1)).save(client);
        verify(clientMapper, times(1)).toEntity(clientDTO);
        verify(clientMapper, times(1)).toDTO(client);
        verify(creditLineStrategy, times(1)).assignCreditLine(anyInt());
    }

    @Test
    void getClientByIdSuccess() {
        Long clientId = 1L;
        Client client = Client.builder()
                .id(clientId)
                .name("Carlos")
                .birthDate(LocalDate.of(1995, 1, 1))
                .creditLine(5000.0)
                .build();

        ClientDTO clientDTO = ClientDTO.builder()
                .id(clientId)
                .name("Carlos")
                .birthDate(client.getBirthDate())
                .creditLine(5000.0)
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.getClientById(clientId);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("Carlos", result.getName());
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientMapper, times(1)).toDTO(client);
    }

    @Test
    void getClientByIdNotFound() {
        Long clientId = 99L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(clientId));
        verify(clientRepository, times(1)).findById(clientId);
    }

}

