package com.poc.parkapi.web.controller;

import com.poc.parkapi.jwt.JwtAuthentication;
import com.poc.parkapi.web.dto.vacancy.CreateVacancyDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vacancies/vacancies-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vacancies/vacancies-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VacancyIT {

    @Autowired
    WebTestClient client;

    @Test
    public void createVacancyWithSuccessAndValidProfile() {
        client
                .post()
                .uri("/api/v1/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(new CreateVacancyDto("A-05", "FREE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void createVacancyWithValidProfileAndInvalidCode() {
        client
                .post()
                .uri("/api/v1/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(new CreateVacancyDto("A-04", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancies");
    }

    @Test
    public void createVacancyWithInvalidProfile() {
        client
                .post()
                .uri("/api/v1/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .bodyValue(new CreateVacancyDto("A-05", "FREE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancies");
    }

    @Test
    public void createVacancyWithValidProfileAndInvalidFields() {
        client
                .post()
                .uri("/api/v1/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(new CreateVacancyDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancies");

        client
                .post()
                .uri("/api/v1/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(new CreateVacancyDto("A-045", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancies");
    }

    @Test
    public void getVacancyWithSuccessAndValidProfile() {
        client
                .get()
                .uri("/api/v1/vacancies/A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("code").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("FREE");
    }

    @Test
    public void getVacancyWithValidProfileAndInvalidCode() {
        client
                .get()
                .uri("/api/v1/vacancies/{code}", "A-06")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vacancies/A-06");
    }

    @Test
    public void getVacancyWithInvalidProfile() {
        client
                .get()
                .uri("/api/v1/vacancies/A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vacancies/A-01");
    }
}
