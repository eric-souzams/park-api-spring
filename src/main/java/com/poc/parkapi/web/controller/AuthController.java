package com.poc.parkapi.web.controller;

import com.poc.parkapi.jwt.JwtToken;
import com.poc.parkapi.jwt.JwtUserDetailsService;
import com.poc.parkapi.web.dto.UserLoginDto;
import com.poc.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Contains all operation related to auth")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;


    @Operation(
            summary = "Authentication",
            description = "Resource to make login",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login with success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtToken.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials",
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
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLoginDto, HttpServletRequest request) {
        log.info("Starting authentication process by {}", userLoginDto.getUsername());

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());
            authenticationManager.authenticate(authenticationToken);

            JwtToken tokenAuthorization = jwtUserDetailsService.getTokenAuthorization(userLoginDto.getUsername());
            return ResponseEntity.ok(tokenAuthorization);
        } catch (AuthenticationException ex) {
            log.warn("Bad credentials from username {}", userLoginDto.getUsername());
        }

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }

}
