package com.example.parkinglot.service;

import com.example.parkinglot.model.enums.SlotType;
import java.math.BigDecimal;

public interface BillingService {
    BigDecimal calculateFee(long durationHours, SlotType slotType);
}