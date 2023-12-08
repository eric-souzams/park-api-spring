package com.poc.parkapi.web.controller;

import com.poc.parkapi.entity.Vacancy;
import com.poc.parkapi.service.VacancyService;
import com.poc.parkapi.web.dto.mapper.VacancyMapper;
import com.poc.parkapi.web.dto.vacancy.CreateVacancyDto;
import com.poc.parkapi.web.dto.vacancy.VacancyResponseDto;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vacancies", description = "Contains all operation related to vacancies")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vacancies")
public class VacancyController {


    private final VacancyService vacancyService;

    @Operation(
            summary = "Create a new vacancy",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to create a new vacancy",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vacancy created with success",
                            headers = @Header(
                                    name = HttpHeaders.LOCATION,
                                    description = "URL from created resource"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Vacancy with code already registered",
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
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateVacancyDto dto) {
        Vacancy vacancy = VacancyMapper.toVacancy(dto);

        vacancyService.save(vacancy);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(vacancy.getCode())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(
            summary = "Create a new vacancy",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to create a new vacancy",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vacancy founded",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VacancyResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacancy not found",
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
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VacancyResponseDto> findByCode(@PathVariable("code") String code) {
        Vacancy result = vacancyService.findByCode(code);

        return ResponseEntity.ok(VacancyMapper.toDto(result));
    }

}
