package com.aplazo.bnpl.service;

import com.aplazo.bnpl.config.CreditLineConfig;
import com.aplazo.bnpl.constants.CreditLineConstants;
import com.aplazo.bnpl.controller.ClientController;
import com.aplazo.bnpl.dto.ClientDto;
import com.aplazo.bnpl.entity.Client;
import com.aplazo.bnpl.entity.UserCredit;
import com.aplazo.bnpl.repository.ClientRepository;
import com.aplazo.bnpl.repository.UserCreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserCreditRepository userCreditRepository;

    @Mock
    private CreditLineConfig creditLineConfig;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterClient() {
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setBirthDate(LocalDate.of(1990, 1, 1));

        UserCredit userCredit = new UserCredit();
        userCredit.setUserId(1L);
        userCredit.setCreditLine(1000.0);
        userCredit.setCreditUtilized(0.0);

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(userCreditRepository.save(any(UserCredit.class))).thenReturn(userCredit);
        when(creditLineConfig.getRanges()).thenReturn(List.of(
                Map.of(CreditLineConstants.MIN_AGE, "18", CreditLineConstants.MAX_AGE, "65", CreditLineConstants.CREDIT_LINE, "1000.0")
        ));

        ClientDto clientDto = clientService.registerClient("John Doe", LocalDate.of(1990, 1, 1));

        assertEquals(1L, clientDto.getId());
        assertEquals(1000.0, clientDto.getCreditLine());
    }

    @Test
    public void testRegisterClientAgeNotAccepted() {
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setBirthDate(LocalDate.of(1900, 1, 1));

        when(creditLineConfig.getRanges()).thenReturn(List.of(
                Map.of(CreditLineConstants.MIN_AGE, "18", CreditLineConstants.MAX_AGE, "65", CreditLineConstants.CREDIT_LINE, "1000.0")
        ));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.registerClient("John Doe", LocalDate.of(1900, 1, 1));
        });

        assertEquals("Client age not accepted", exception.getMessage());
    }
}