package com.poc.parkapi.repository;

import com.poc.parkapi.entity.Client;
import com.poc.parkapi.repository.projection.ClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c")
    Page<ClientProjection> findAllClients(Pageable pageable);

    Client findByUserId(Long id);
}