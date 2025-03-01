package com.bnpl.mapper;

import com.bnpl.domain.Client;
import com.bnpl.dto.ClientDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-28T21:30:53-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientDTO toDTO(Client client) {
        if ( client == null ) {
            return null;
        }

        ClientDTO.ClientDTOBuilder clientDTO = ClientDTO.builder();

        clientDTO.id( client.getId() );
        clientDTO.name( client.getName() );
        clientDTO.birthDate( client.getBirthDate() );
        clientDTO.creditLine( client.getCreditLine() );

        return clientDTO.build();
    }

    @Override
    public Client toEntity(ClientDTO clientDTO) {
        if ( clientDTO == null ) {
            return null;
        }

        Client.ClientBuilder client = Client.builder();

        client.id( clientDTO.getId() );
        client.name( clientDTO.getName() );
        client.birthDate( clientDTO.getBirthDate() );
        client.creditLine( clientDTO.getCreditLine() );

        return client.build();
    }
}
