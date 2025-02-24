package com.aplazo.bnpl.dto;

import com.aplazo.bnpl.entity.Client;
import lombok.Data;

@Data
public class ClientResponse {
    private Long id;
    private Double creditLine;

    public ClientResponse set(ClientDto clientDto) {
        this.id = clientDto.getId();
        this.creditLine = clientDto.getCreditLine();
        return this;
    }
}
