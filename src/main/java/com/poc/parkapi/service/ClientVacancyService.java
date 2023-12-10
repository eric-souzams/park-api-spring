package com.poc.parkapi.service;

import com.poc.parkapi.entity.ClientVacancy;
import com.poc.parkapi.repository.ClientVacancyRepository;
import com.poc.parkapi.repository.projection.ClientVacancyProjection;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public ClientVacancy findByReceipt(String receipt) {
        return clientVacancyRepository.findByReceiptAndDepartureDateIsNull(receipt)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Receipt '%s' not found or check-out already did", receipt)));
    }

    @Transactional(readOnly = true)
    public long getTotalTimesOfCompleteVacancy(String cpf) {
        return clientVacancyRepository.countByClientCpfAndDepartureDateIsNotNull(cpf);
    }

    @Transactional
    public Page<ClientVacancyProjection> findAllByClientCpf(String cpf, Pageable pageable) {
        return clientVacancyRepository.findAllByClientCpf(cpf, pageable);
    }

    @Transactional
    public Page<ClientVacancyProjection> findAllFromUserId(Long id, Pageable pageable) {
        return clientVacancyRepository.findAllByClientUserId(id, pageable);
    }
}
