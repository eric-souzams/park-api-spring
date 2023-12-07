package com.poc.parkapi.web.controller;

import com.poc.parkapi.entity.Client;
import com.poc.parkapi.jwt.JwtUserDetails;
import com.poc.parkapi.repository.projection.ClientProjection;
import com.poc.parkapi.service.ClientService;
import com.poc.parkapi.service.UserService;
import com.poc.parkapi.web.dto.client.CreateClientDto;
import com.poc.parkapi.web.dto.client.ClientResponseDto;
import com.poc.parkapi.web.dto.mapper.ClientMapper;
import com.poc.parkapi.web.dto.mapper.PageableMapper;
import com.poc.parkapi.web.dto.pageable.PageableDto;
import com.poc.parkapi.web.dto.user.UserResponseDto;
import com.poc.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clients", description = "Contains all operation related to clients")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @Operation(
            summary = "Create a new client",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to create a new client",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client created with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClientResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Client with CPF already registered",
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
                            description = "Resource only allowed to CLIENT profile",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ClientResponseDto> create(@RequestBody @Valid CreateClientDto clientDto,
                                                    @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(clientDto);
        client.setUser(userService.findById(userDetails.getId()));

        clientService.save(client);

        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponseDto(client));
    }

    @Operation(
            summary = "Find client",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to get/find a client by id. Only to ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client found with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClientResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found",
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable("id") Long id) {
        Client result = clientService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ClientMapper.toResponseDto(result));
    }

    @Operation(
            summary = "Get details",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to get details. Only to CLIENT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client details returned with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClientResponseDto.class)
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
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/details")
    public ResponseEntity<ClientResponseDto> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Client result = clientService.findByUserId(userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ClientMapper.toResponseDto(result));
    }

    @Operation(
            summary = "Get all clients",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to get all clients. Only to ADMIN",
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
                                            defaultValue = "20",
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
                                            defaultValue = "id,asc",
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
                                            schema = @Schema(implementation = ClientResponseDto.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User without permission",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableDto> getAllClients(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        Page<ClientProjection> result = clientService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageableMapper.toDto(result));
    }

}
