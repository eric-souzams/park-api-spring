package com.poc.parkapi.repository;

import com.poc.parkapi.entity.Vacancy;
import com.poc.parkapi.enums.StatusVacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    Optional<Vacancy> findByCode(String code);

    Optional<Vacancy> findFirstByStatus(StatusVacancy statusVacancy);
}