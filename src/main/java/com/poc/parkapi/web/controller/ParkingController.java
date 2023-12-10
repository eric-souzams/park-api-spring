package com.poc.parkapi.web.controller;

import com.poc.parkapi.entity.ClientVacancy;
import com.poc.parkapi.jwt.JwtUserDetails;
import com.poc.parkapi.repository.projection.ClientVacancyProjection;
import com.poc.parkapi.service.ClientVacancyService;
import com.poc.parkapi.service.ParkingService;
import com.poc.parkapi.web.dto.client.ClientResponseDto;
import com.poc.parkapi.web.dto.mapper.ClientVacancyMapper;
import com.poc.parkapi.web.dto.mapper.PageableMapper;
import com.poc.parkapi.web.dto.pageable.PageableDto;
import com.poc.parkapi.web.dto.parking.CreateParkingDto;
import com.poc.parkapi.web.dto.parking.ParkingResponseDto;
import com.poc.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Parking", description = "Contains all operation related to parking")
@RequiredArgsConstructor
@RequestMapping("/api/v1/parking")
@RestController
public class ParkingController {

    private final ParkingService parkingService;

    private final ClientVacancyService clientVacancyService;

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


    @Operation(
            summary = "Find parking",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to find a parking",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "receipt",
                            description = "Number of the receipt generated on check-in"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parking founded with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ParkingResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Receipt not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping("/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ParkingResponseDto> getByReceipt(@PathVariable("receipt") String receipt) {
        ClientVacancy clientVacancy = clientVacancyService.findByReceipt(receipt);

        return ResponseEntity.ok(ClientVacancyMapper.toDto(clientVacancy));
    }

    @Operation(
            summary = "Find parking",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to find a parking",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "receipt",
                            description = "Number of the receipt generated on check-in"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parking founded with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ParkingResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Receipt not found or check-out already did",
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
    @PutMapping("/check-out/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkOut(@PathVariable("receipt") String receipt) {
        ClientVacancy clientVacancy = parkingService.checkOut(receipt);

        return ResponseEntity.ok(ClientVacancyMapper.toDto(clientVacancy));
    }

    @Operation(
            summary = "Get all parking by cpf",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to get all parking by cpf. Only to ADMIN",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            description = "Client CPF",
                            name = "cpf",
                            required = true
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "page",
                            content = @Content(
                                    schema = @Schema(
                                            type = "integer",
                                            defaultValue = "0",
                                            description = "Response page"
                                    )
                            )
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "size",
                            content = @Content(
                                    schema = @Schema(
                                            type = "integer",
                                            defaultValue = "5",
                                            description = "Total elements per page"
                                    )
                            )
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "sort",
                            hidden = true,
                            array = @ArraySchema(
                                    schema = @Schema(
                                            type = "string",
                                            defaultValue = "entryDate,asc",
                                            description = "Sort type of the elements"
                                    )
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Parking listed with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ParkingResponseDto.class)
                                    )
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
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getParkingByCpf(@PathVariable("cpf") String cpf,
                                                       @PageableDefault(size = 5, sort = "entryDate",
                                                               direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ClientVacancyProjection> projection = clientVacancyService.findAllByClientCpf(cpf, pageable);

        PageableDto dto = PageableMapper.toDto(projection);

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get all parking logged user",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to get all parking from logged user. Only to CLIENT",
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "page",
                            content = @Content(
                                    schema = @Schema(
                                            type = "integer",
                                            defaultValue = "0",
                                            description = "Response page"
                                    )
                            )
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "size",
                            content = @Content(
                                    schema = @Schema(
                                            type = "integer",
                                            defaultValue = "5",
                                            description = "Total elements per page"
                                    )
                            )
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "sort",
                            hidden = true,
                            array = @ArraySchema(
                                    schema = @Schema(
                                            type = "string",
                                            defaultValue = "entryDate,asc",
                                            description = "Sort type of the elements"
                                    )
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users listed with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ParkingResponseDto.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Parking only allowed to CLIENT profile",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PageableDto> getParkingFromClient(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                       @PageableDefault(size = 5, sort = "entryDate",
                                                               direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ClientVacancyProjection> projection = clientVacancyService.findAllFromUserId(userDetails.getId(), pageable);

        PageableDto dto = PageableMapper.toDto(projection);

        return ResponseEntity.ok(dto);
    }
}
