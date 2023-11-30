package com.poc.parkapi.web.controller;

import com.poc.parkapi.jwt.JwtAuthentication;
import com.poc.parkapi.web.dto.CreateUserDto;
import com.poc.parkapi.web.dto.UpdateUserPasswordDto;
import com.poc.parkapi.web.dto.UserResponseDto;
import com.poc.parkapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

    @Autowired
    WebTestClient client;

    @Test
    public void createUserWithSuccessWithValidUsernameAndPassword() {
        UserResponseDto responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("andre@poc.dev", "456789"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getUsername()).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo("andre@poc.dev");
        assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void createUserWithInvalidUsername() {
        ErrorMessage responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("", "456789"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("lucas@", "456789"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("lucas@poc.", "456789"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUserWithInvalidPassword() {
        ErrorMessage responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("andre-luis@poc.dev", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("andre-luis@poc.dev", "123"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("andre-luis@poc.dev", "4567897"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUserWithUsernameAlreadyRegistered() {
        ErrorMessage responseBody = client
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto("lucas@poc.dev", "456789"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void findUserByIdWithValidId() {
        UserResponseDto responseBody = client
                .get()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getUsername()).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo("maria@poc.dev");
        assertThat(responseBody.getId()).isEqualTo(100);
        assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = client
                .get()
                .uri("/api/v1/users/200")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getUsername()).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo("joao@poc.dev");
        assertThat(responseBody.getId()).isEqualTo(200);
        assertThat(responseBody.getRole()).isEqualTo("CLIENT");

        responseBody = client
                .get()
                .uri("/api/v1/users/200")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getUsername()).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo("joao@poc.dev");
        assertThat(responseBody.getId()).isEqualTo(200);
        assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void findUserByIdWithInvalidIdAndAdminRole() {
        ErrorMessage responseBody = client
                .get()
                .uri("/api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void findUserByIdWithValidIdAndClientRole() {
        ErrorMessage responseBody = client
                .get()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void updateUserPasswordWithSuccessWithValidPasswords() {
        client
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("123456", "123457", "123457"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void updateUserPasswordWithSuccessWithValidPasswordsAndInvalidUserId() {
        ErrorMessage responseBody = client
                .patch()
                .uri("/api/v1/users/200")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("123456", "123457", "123457"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void updateUserPasswordWithDifferentPasswordsAndValidUserId() {
        ErrorMessage responseBody = client
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("123456", "123457", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = client
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("123459", "123456", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void updateUserPasswordWithDifferentPasswordSizeAndValidUserId() {
        ErrorMessage responseBody = client
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("123456", "12345", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("123456", "1234567", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto("", "123456", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void getAllUserRegistered() {
        List<UserResponseDto> responseBody = client
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(5);
    }

    @Test
    public void getAllUserRegisteredWithClientRole() {
        ErrorMessage responseBody = client
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
