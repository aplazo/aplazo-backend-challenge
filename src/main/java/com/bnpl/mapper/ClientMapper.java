package com.bnpl.mapper;

import com.bnpl.domain.Client;
import com.bnpl.dto.ClientDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDTO(Client client);
    Client toEntity(ClientDTO clientDTO);
}
