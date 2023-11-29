package com.poc.parkapi.web.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CreateUserDto {

    private String username;
    private String password;

}
