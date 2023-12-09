package com.poc.parkapi.service;

import com.poc.parkapi.entity.ClientVacancy;
import com.poc.parkapi.repository.ClientVacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientVacancyService {

    private final ClientVacancyRepository clientVacancyRepository;

    @Transactional
    public ClientVacancy save(ClientVacancy clientVacancy) {
        return clientVacancyRepository.save(clientVacancy);
    }

}
