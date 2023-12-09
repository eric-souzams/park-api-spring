package com.poc.parkapi.web.controller;

import com.poc.parkapi.entity.ClientVacancy;
import com.poc.parkapi.service.ParkingService;
import com.poc.parkapi.web.dto.mapper.ClientVacancyMapper;
import com.poc.parkapi.web.dto.parking.CreateParkingDto;
import com.poc.parkapi.web.dto.parking.ParkingResponseDto;
import com.poc.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Parking", description = "Contains all operation related to parking")
@RequiredArgsConstructor
@RequestMapping("/api/v1/parking")
@RestController
public class ParkingController {

    private final ParkingService parkingService;

    @Operation(
            summary = "Create a new parking",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to create a new parking",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Parking created with success",
                            headers = @Header(
                                    name = HttpHeaders.LOCATION,
                                    description = "URL to access created parking"
                            ),
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ParkingResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client CPF or free vacancy not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Invalid field(s)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Resource only allowed to ADMIN profile",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkIn(@RequestBody @Valid CreateParkingDto dto) {
        ClientVacancy clientVacancy = ClientVacancyMapper.toClientVacancy(dto);

        parkingService.checkIn(clientVacancy);

        ParkingResponseDto responseDto = ClientVacancyMapper.toDto(clientVacancy);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientVacancy.getReceipt())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }
}
