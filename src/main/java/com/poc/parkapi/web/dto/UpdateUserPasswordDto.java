package com.poc.parkapi.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserPasswordDto {

    private String password;
    private String newPassword;
    private String confirmNewPassword;

}
