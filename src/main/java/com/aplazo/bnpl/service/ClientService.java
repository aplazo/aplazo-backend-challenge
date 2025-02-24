package com.aplazo.bnpl.service;

import com.aplazo.bnpl.config.CreditLineConfig;
import com.aplazo.bnpl.constants.CreditLineConstants;
import com.aplazo.bnpl.dto.ClientDto;
import com.aplazo.bnpl.entity.Client;
import com.aplazo.bnpl.entity.UserCredit;
import com.aplazo.bnpl.repository.ClientRepository;
import com.aplazo.bnpl.repository.UserCreditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserCreditRepository userCreditRepository;

    @Autowired
    private CreditLineConfig creditLineConfig;

    @Transactional
    public ClientDto registerClient(String name, LocalDate birthDate) {

        logger.info("Registering client : {} {} ", name, birthDate);

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        Double creditLine = calculateCreditLine(age);

        if (creditLine == null) {
            logger.error("Client age not accepted: {}", age);
            throw new IllegalArgumentException("Client age not accepted");
        }

        Client client = new Client();
        client.setName(name);
        client.setBirthDate(birthDate);

        Client savedClient = clientRepository.save(client);

        UserCredit userCredit = new UserCredit();
        userCredit.setUserId(savedClient.getId());
        userCredit.setCreditLine(creditLine);
        userCredit.setCreditUtilized(0.0);
        userCreditRepository.save(userCredit);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(savedClient.getId());
        clientDto.setCreditLine(userCredit.getCreditLine());

        return clientDto;


    }

    private Double calculateCreditLine(int age) {
        List<Map<String, String>> ranges = creditLineConfig.getRanges();
        for (Map<String, String> range : ranges) {
            int minAge = Integer.parseInt(range.get(CreditLineConstants.MIN_AGE));
            int maxAge = Integer.parseInt(range.get(CreditLineConstants.MAX_AGE));
            if (age >= minAge && age <= maxAge) {
                return Double.parseDouble(range.get(CreditLineConstants.CREDIT_LINE));
            }
        }
        return null;
    }
}
