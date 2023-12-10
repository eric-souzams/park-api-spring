package com.poc.parkapi.service;

import com.poc.parkapi.entity.Client;
import com.poc.parkapi.entity.ClientVacancy;
import com.poc.parkapi.entity.Vacancy;
import com.poc.parkapi.enums.StatusVacancy;
import com.poc.parkapi.util.ParkingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ParkingService {

    private final ClientVacancyService clientVacancyService;

    private final ClientService clientService;

    private final VacancyService vacancyService;

    @Transactional
    public ClientVacancy checkIn(ClientVacancy clientVacancy) {
        Client client = clientService.findByCpf(clientVacancy.getClient().getCpf());
        clientVacancy.setClient(client);

        Vacancy vacancy = vacancyService.findByFreeVacancy();
        vacancy.setStatus(StatusVacancy.BUSY);
        clientVacancy.setVacancy(vacancy);

        clientVacancy.setEntryDate(LocalDateTime.now());
        clientVacancy.setReceipt(ParkingUtils.generateReceipt());

        return clientVacancyService.save(clientVacancy);
    }

    @Transactional
    public ClientVacancy checkOut(String receipt) {
        ClientVacancy result = clientVacancyService.findByReceipt(receipt);

        LocalDateTime departureDate = LocalDateTime.now();
        BigDecimal amount = ParkingUtils.calcTotal(result.getEntryDate(), departureDate);
        result.setAmount(amount);

        long numberOfTimes = clientVacancyService.getTotalTimesOfCompleteVacancy(result.getClient().getCpf());

        BigDecimal discount = ParkingUtils.calcDiscount(amount, numberOfTimes);
        result.setDiscount(discount);

        result.setDepartureDate(departureDate);
        result.getVacancy().setStatus(StatusVacancy.FREE);

        return clientVacancyService.save(result);
    }
}
