package com.aplazo.bnpl;

import com.aplazo.bnpl.entity.Client;
import com.aplazo.bnpl.entity.Purchase;
import com.aplazo.bnpl.entity.UserCredit;
import com.aplazo.bnpl.repository.ClientRepository;
import com.aplazo.bnpl.repository.UserCreditRepository;
import com.aplazo.bnpl.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PurchaseServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserCreditRepository userCreditRepository;

    @Test
    public void testRegisterPurchaseIntegration() {
        Client client = new Client();
        client.setName("John Doe");
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        client = clientRepository.save(client);

        UserCredit userCredit = new UserCredit();
        userCredit.setUserId(client.getId());
        userCredit.setCreditLine(1000.0);
        userCredit.setCreditUtilized(0.0);
        userCredit = userCreditRepository.save(userCredit);

        Purchase purchase = purchaseService.registerPurchase(client.getId(), 500.0);

        assertEquals(client.getId(), purchase.getClient().getId());
        assertEquals(500.0, purchase.getAmount());
    }
}