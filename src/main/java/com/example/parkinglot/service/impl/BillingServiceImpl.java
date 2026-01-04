package com.example.parkinglot.service.impl;

import com.example.parkinglot.service.BillingService;
import com.example.parkinglot.model.enums.SlotType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BillingServiceImpl implements BillingService {

    private static final long BASE_HOURS = 2;
    private static final BigDecimal BASE_FEE = new BigDecimal("16.00");

    @Override
    public BigDecimal calculateFee(long durationHours, SlotType slotType) {

        if (durationHours <= BASE_HOURS) {
            return BASE_FEE;
        }
        long extraHours = durationHours - BASE_HOURS;
        BigDecimal hourlyRate = getHourlyRate(slotType);

        BigDecimal extraFee = hourlyRate.multiply(BigDecimal.valueOf(extraHours));
        BigDecimal totalFee = BASE_FEE.add(extraFee);

        log.info(">>计算总金额: {}", totalFee);
        return totalFee;
    }

    private BigDecimal getHourlyRate(SlotType slotType) {
        return switch (slotType) {
            case STANDARD -> new BigDecimal("8.00");
            case VIP      -> new BigDecimal("10.00");
            case LARGE    -> new BigDecimal("9.00");
        };
    }
}