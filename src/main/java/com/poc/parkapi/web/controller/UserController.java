package com.poc.parkapi.web.controller;

import com.poc.parkapi.entity.User;
import com.poc.parkapi.service.UserService;
import com.poc.parkapi.web.dto.CreateUserDto;
import com.poc.parkapi.web.dto.UpdateUserPasswordDto;
import com.poc.parkapi.web.dto.UserResponseDto;
import com.poc.parkapi.web.dto.mapper.UserMapper;
import com.poc.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Contains all operation related to user")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Resource to create a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already registered",
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
                    )
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid CreateUserDto createUserDto) {
        User response = userService.save(UserMapper.toUser(createUserDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDto(response));
    }


    @Operation(
            summary = "Find a user by ID",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to find a user by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User founded with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not founded",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
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
                    ,
                    @ApiResponse(
                            responseCode = "401",
                            description = "User unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENT') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        User response = userService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toResponseDto(response));
    }

    @Operation(
            summary = "Get all users",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to get all users. Only to ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users listed with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserResponseDto.class)
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
                    ,
                    @ApiResponse(
                            responseCode = "401",
                            description = "User unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> response = userService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toResponseListDto(response));
    }

    @Operation(
            summary = "Update user password",
            security = @SecurityRequirement(name = "security"),
            description = "Resource to update user password",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "User password updated with success"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User password not match",
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
                            description = "User without permission",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
                    ,
                    @ApiResponse(
                            responseCode = "401",
                            description = "User unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword(@PathVariable("id") Long id, @RequestBody @Valid UpdateUserPasswordDto updateUserPasswordDto) {
        userService.updatePassword(id, updateUserPasswordDto.getPassword(), updateUserPasswordDto.getNewPassword(), updateUserPasswordDto.getConfirmNewPassword());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
