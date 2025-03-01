package com.bnpl.mapper;

import com.bnpl.domain.Purchase;
import com.bnpl.dto.PurchaseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(source = "client.id", target = "clientId")
    PurchaseDTO toDTO(Purchase purchase);

    @Mapping(source = "clientId", target = "client.id")
    Purchase toEntity(PurchaseDTO purchaseDTO);
}

