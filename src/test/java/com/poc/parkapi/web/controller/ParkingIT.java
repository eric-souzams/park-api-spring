package com.poc.parkapi.web.controller;

import com.poc.parkapi.jwt.JwtAuthentication;
import com.poc.parkapi.web.dto.parking.CreateParkingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parking/parking-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking/parking-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient client;

    @Test
    public void createCheckInWithSuccessAndValidFields() {
        CreateParkingDto dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("Siena")
                .color("RED").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("plate").isEqualTo("YUI-9632")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("Siena")
                .jsonPath("color").isEqualTo("RED")
                .jsonPath("clientCpf").isEqualTo("79074426050")
                .jsonPath("receipt").exists()
                .jsonPath("entryDate").exists()
                .jsonPath("vacancyCode").exists();
    }

    @Test
    public void createCheckInWithInvalidProfile() {
        CreateParkingDto dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("Siena")
                .color("RED").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "joao@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");
    }

    @Test
    public void createCheckInWithInvalidFields() {
        CreateParkingDto dto = CreateParkingDto.builder().plate("").brand("").model("").color("").clientCpf("").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");

        dto = CreateParkingDto.builder().plate("YU7-9632").brand("FIAT").model("Siena")
                .color("RED").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");

        dto = CreateParkingDto.builder().plate("YUI-963A").brand("FIAT").model("Siena")
                .color("RED").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");

        dto = CreateParkingDto.builder().plate("YUI-9632").brand("").model("Siena")
                .color("RED").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");

        dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("")
                .color("RED").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");

        dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("Siena")
                .color("").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");

        dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("Siena")
                .color("RED").clientCpf("7907446050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");

        dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("Siena")
                .color("RED").clientCpf("790744260500").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");
    }

    @Test
    public void createCheckInWithValidProfileAndInvalidClient() {
        CreateParkingDto dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("Siena")
                .color("RED").clientCpf("33838667000").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");
    }

    @Sql(scripts = "/sql/parking/parking-insert-busy-vacancies.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parking/parking-delete-busy-vacancies.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCheckInWithValidProfileAndWithoutFreeVacancy() {
        CreateParkingDto dto = CreateParkingDto.builder().plate("YUI-9632").brand("FIAT").model("Siena")
                .color("RED").clientCpf("79074426050").build();

        client.post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(client, "maria@poc.dev", "123456"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in");
    }

}
