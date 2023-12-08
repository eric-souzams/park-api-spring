package com.poc.parkapi.web.dto.vacancy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VacancyResponseDto {

    private Long id;

    private String code;

    private String status;

}
