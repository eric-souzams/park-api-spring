package com.poc.parkapi.repository;

import com.poc.parkapi.entity.ClientVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
}