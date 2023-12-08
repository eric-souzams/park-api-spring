package com.poc.parkapi.web.dto.mapper;

import com.poc.parkapi.entity.Vacancy;
import com.poc.parkapi.web.dto.vacancy.CreateVacancyDto;
import com.poc.parkapi.web.dto.vacancy.VacancyResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacancyMapper {

    public static Vacancy toVacancy(CreateVacancyDto dto) {
        return new ModelMapper().map(dto, Vacancy.class);
    }

    public static VacancyResponseDto toDto(Vacancy vacancy) {
        return new ModelMapper().map(vacancy, VacancyResponseDto.class);
    }

}
