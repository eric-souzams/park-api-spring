package com.poc.parkapi.web.dto.mapper;

import com.poc.parkapi.entity.ClientVacancy;
import com.poc.parkapi.web.dto.parking.CreateParkingDto;
import com.poc.parkapi.web.dto.parking.ParkingResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientVacancyMapper {

    public static ClientVacancy toClientVacancy(CreateParkingDto dto) {
        return new ModelMapper().map(dto, ClientVacancy.class);
    }

    public static ParkingResponseDto toDto(ClientVacancy clientVacancy) {
        return new ModelMapper().map(clientVacancy, ParkingResponseDto.class);
    }

}
