package com.poc.parkapi.service;

import com.poc.parkapi.entity.Vacancy;
import com.poc.parkapi.repository.VacancyRepository;
import com.poc.parkapi.web.exception.CodeUniqueViolationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.poc.parkapi.enums.StatusVacancy.FREE;

@RequiredArgsConstructor
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    @Transactional
    public Vacancy save(Vacancy vacancy) {
        try {
            return vacancyRepository.save(vacancy);
        } catch (DataIntegrityViolationException ex) {
            throw new CodeUniqueViolationException(String.format("Vacancy with code '%s' already registered", vacancy.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public Vacancy findByCode(String code) {
        return vacancyRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Vacancy with code=%s not founded", code)));
    }

    @Transactional(readOnly = true)
    public Vacancy findByFreeVacancy() {
        return vacancyRepository.findFirstByStatus(FREE)
                .orElseThrow(() -> new EntityNotFoundException("No free vacancies were found."));
    }
}
