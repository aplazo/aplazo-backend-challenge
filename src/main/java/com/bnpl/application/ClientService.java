package com.bnpl.application;

import com.bnpl.domain.Client;
import com.bnpl.dto.ClientDTO;
import com.bnpl.exceptions.ClientNotFoundException;
import com.bnpl.infrastructure.repository.ClientRepository;
import com.bnpl.mapper.ClientMapper;
import com.bnpl.strategy.CreditLineStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final CreditLineStrategy creditLineStrategy;

    public ClientDTO registerClient(ClientDTO clientDTO) {
        int age = Period.between(clientDTO.getBirthDate(), LocalDate.now()).getYears();
        double creditLine = creditLineStrategy.assignCreditLine(age);

        Client client = clientMapper.toEntity(clientDTO);
        client.setCreditLine(creditLine);
        clientRepository.save(client);

        return clientMapper.toDTO(client);
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        return clientMapper.toDTO(client);
    }
}
