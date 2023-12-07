package com.poc.parkapi.web.dto.mapper;

import com.poc.parkapi.entity.Client;
import com.poc.parkapi.web.dto.client.CreateClientDto;
import com.poc.parkapi.web.dto.client.ClientResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client toClient(CreateClientDto createClientDto) {
        return new ModelMapper().map(createClientDto, Client.class);
    }

    public static ClientResponseDto toResponseDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }


}
