package com.poc.parkapi.service;

import com.poc.parkapi.entity.Client;
import com.poc.parkapi.repository.ClientRepository;
import com.poc.parkapi.repository.projection.ClientProjection;
import com.poc.parkapi.web.exception.CpfUniqueViolationException;
import com.poc.parkapi.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client save(Client client) {
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(String
                    .format("CPF '%s' can't be registered, already in use", client.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Client with id=%s not founded", id)));
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> findAll(Pageable pageable) {
        return clientRepository.findAllClients(pageable);
    }

    @Transactional(readOnly = true)
    public Client findByUserId(Long id) {
        return clientRepository.findByUserId(id);
    }
}
