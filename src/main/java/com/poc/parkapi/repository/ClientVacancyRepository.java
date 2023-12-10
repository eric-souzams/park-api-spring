package com.poc.parkapi.repository;

import com.poc.parkapi.entity.ClientVacancy;
import com.poc.parkapi.repository.projection.ClientVacancyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
    Optional<ClientVacancy> findByReceiptAndDepartureDateIsNull(String receipt);

    long countByClientCpfAndDepartureDateIsNotNull(String cpf);

    Page<ClientVacancyProjection> findAllByClientCpf(String cpf, Pageable pageable);

    Page<ClientVacancyProjection> findAllByClientUserId(Long id, Pageable pageable);
}