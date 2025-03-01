package com.bnpl.mapper;

import com.bnpl.domain.Client;
import com.bnpl.domain.Purchase;
import com.bnpl.dto.PurchaseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-28T21:30:53-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class PurchaseMapperImpl implements PurchaseMapper {

    @Override
    public PurchaseDTO toDTO(Purchase purchase) {
        if ( purchase == null ) {
            return null;
        }

        PurchaseDTO.PurchaseDTOBuilder purchaseDTO = PurchaseDTO.builder();

        purchaseDTO.clientId( purchaseClientId( purchase ) );
        purchaseDTO.id( purchase.getId() );
        purchaseDTO.purchaseAmount( purchase.getPurchaseAmount() );
        purchaseDTO.commissionAmount( purchase.getCommissionAmount() );
        purchaseDTO.totalAmount( purchase.getTotalAmount() );
        purchaseDTO.numberOfPayments( purchase.getNumberOfPayments() );
        purchaseDTO.purchaseDate( purchase.getPurchaseDate() );

        return purchaseDTO.build();
    }

    @Override
    public Purchase toEntity(PurchaseDTO purchaseDTO) {
        if ( purchaseDTO == null ) {
            return null;
        }

        Purchase.PurchaseBuilder purchase = Purchase.builder();

        purchase.client( purchaseDTOToClient( purchaseDTO ) );
        purchase.id( purchaseDTO.getId() );
        purchase.purchaseAmount( purchaseDTO.getPurchaseAmount() );
        purchase.commissionAmount( purchaseDTO.getCommissionAmount() );
        purchase.totalAmount( purchaseDTO.getTotalAmount() );
        purchase.numberOfPayments( purchaseDTO.getNumberOfPayments() );
        purchase.purchaseDate( purchaseDTO.getPurchaseDate() );

        return purchase.build();
    }

    private Long purchaseClientId(Purchase purchase) {
        if ( purchase == null ) {
            return null;
        }
        Client client = purchase.getClient();
        if ( client == null ) {
            return null;
        }
        Long id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Client purchaseDTOToClient(PurchaseDTO purchaseDTO) {
        if ( purchaseDTO == null ) {
            return null;
        }

        Client.ClientBuilder client = Client.builder();

        client.id( purchaseDTO.getClientId() );

        return client.build();
    }
}
