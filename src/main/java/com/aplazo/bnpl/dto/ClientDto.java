package com.aplazo.bnpl.dto;

import com.aplazo.bnpl.entity.Client;
import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private Double creditLine;
}
