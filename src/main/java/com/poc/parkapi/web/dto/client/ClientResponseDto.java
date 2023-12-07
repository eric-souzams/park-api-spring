package com.poc.parkapi.web.dto.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientResponseDto {

    private Long id;

    private String name;

    private String cpf;

}
