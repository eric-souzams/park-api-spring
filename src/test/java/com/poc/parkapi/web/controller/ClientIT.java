package com.poc.parkapi.web.controller;

import com.poc.parkapi.jwt.JwtAuthentication;
import com.poc.parkapi.web.dto.client.CreateClientDto;
import com.poc.parkapi.web.dto.client.ClientResponseDto;
import com.poc.parkapi.web.dto.pageable.PageableDto;
import com.poc.parkapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clients/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {

    @Autowired
    WebTestClient client;

    @Test
    public void createClientWithSuccessAndValidCredentials() {
        ClientResponseDto responseBody = client
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .bodyValue(new CreateClientDto("Joao Test", "55352517047"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getName()).isEqualTo("Joao Test");
        assertThat(responseBody.getCpf()).isEqualTo("55352517047");
    }

    @Test
    public void createClientWithRegisteredCPF() {
        ErrorMessage responseBody = client
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .bodyValue(new CreateClientDto("Joao Test", "79074426050"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void createClientWithInvalidFields() {
        ErrorMessage responseBody = client
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .bodyValue(new CreateClientDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .bodyValue(new CreateClientDto("Jo", "79074426051"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = client
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .bodyValue(new CreateClientDto("Jo", "790.744.260-50"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createClientWithInvalidProfile() {
        ErrorMessage responseBody = client
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(new CreateClientDto("Joao Test", "79074426050"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void findClientWithSuccessAndValidProfile() {
        ClientResponseDto responseBody = client
                .get()
                .uri("/api/v1/clients/10")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(10);
        assertThat(responseBody.getName()).isEqualTo("Maira");
        assertThat(responseBody.getCpf()).isEqualTo("85212472067");
    }

    @Test
    public void findClientWithInvalidProfile() {
        ErrorMessage responseBody = client
                .get()
                .uri("/api/v1/clients/10")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void findClientWithInvalidId() {
        ErrorMessage responseBody = client
                .get()
                .uri("/api/v1/clients/11")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void getAllClientsWithSuccessAndValidProfile() {
        PageableDto responseBody = client
                .get()
                .uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getContent().size()).isEqualTo(2);
        assertThat(responseBody.getNumber()).isEqualTo(0);
        assertThat(responseBody.getTotalPages()).isEqualTo(1);

        responseBody = client
                .get()
                .uri("/api/v1/clients?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getContent().size()).isEqualTo(1);
        assertThat(responseBody.getNumber()).isEqualTo(1);
        assertThat(responseBody.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void getAllClientsWithInvalidProfile() {
        ErrorMessage responseBody = client
                .get()
                .uri("/api/v1/clients")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void getClientDetailsWithSuccessAndValidProfile() {
        ClientResponseDto responseBody = client
                .get()
                .uri("/api/v1/clients/details")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "mateus@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(20);
        assertThat(responseBody.getName()).isEqualTo("Joao");
    }

    @Test
    public void getClientDetailsWithInvalidProfile() {
        ErrorMessage responseBody = client
                .get()
                .uri("/api/v1/clients/details")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(403);
    }

}
