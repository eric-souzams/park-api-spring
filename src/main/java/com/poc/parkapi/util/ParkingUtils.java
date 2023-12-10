package com.poc.parkapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    private static final double FIRST_15_MINUTES = 5.00;
    private static final double FIRST_60_MINUTES = 9.25;
    private static final double ADDITIONAL_15_MINUTES = 1.75;
    private static final double DISCOUNT_PERCENT = 0.30;


    public static String generateReceipt() {
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0, 19);

        return receipt.replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }

    public static BigDecimal calcTotal(LocalDateTime checkIn, LocalDateTime checkOut) {
        long minutes = checkIn.until(checkOut, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = FIRST_15_MINUTES;
        } else if (minutes <= 60) {
            total = FIRST_60_MINUTES;
        } else {
            long additionalMinutes = minutes - 60;
            double totalParts = ((double) additionalMinutes / 15);
            if (totalParts > (int) totalParts) {
                total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * ((int) totalParts + 1));
            } else {
                total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * (int) totalParts);
            }
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcDiscount(BigDecimal amount, long numberOfTimes) {
        BigDecimal discount = ((numberOfTimes > 0) && (numberOfTimes % 10 == 0))
                ? amount.multiply(new BigDecimal(DISCOUNT_PERCENT))
                : new BigDecimal(0);
        return discount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
