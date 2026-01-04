package com.example.parkinglot.service.impl;

import com.example.parkinglot.model.enums.SlotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BillingServiceImplTest {

    private BillingServiceImpl billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingServiceImpl();
    }

    @Test
    void shouldReturnBaseFeeWhenDurationIs1Hour() {
        BigDecimal fee = billingService.calculateFee(1, SlotType.STANDARD);
        assertEquals(new BigDecimal("16.00"), fee);
    }

    @Test
    void shouldReturnBaseFeeWhenDurationIs2Hours() {
        BigDecimal fee = billingService.calculateFee(2, SlotType.STANDARD);
        assertEquals(new BigDecimal("16.00"), fee);
    }

    @Test
    void shouldCalculateExtraFeeWhenDurationIs3Hours_Standard() {
        BigDecimal fee = billingService.calculateFee(3, SlotType.STANDARD);
        // 16.00 + (3-2) * 8.00 = 24.00
        assertEquals(new BigDecimal("24.00"), fee);
    }

    @Test
    void shouldCalculateExtraFeeWhenDurationIs3Hours_VIP() {
        BigDecimal fee = billingService.calculateFee(3, SlotType.VIP);
        // 16.00 + (3-2) * 10.00 = 26.00
        assertEquals(new BigDecimal("26.00"), fee);
    }

    @Test
    void shouldCalculateExtraFeeWhenDurationIs3Hours_Large() {
        BigDecimal fee = billingService.calculateFee(3, SlotType.LARGE);
        // 16.00 + (3-2) * 9.00 = 25.00
        assertEquals(new BigDecimal("25.00"), fee);
    }

    @Test
    void shouldCalculateExtraFeeWhenDurationIs5Hours_Standard() {
        BigDecimal fee = billingService.calculateFee(5, SlotType.STANDARD);
        // 16.00 + (5-2) * 8.00 = 40.00
        assertEquals(new BigDecimal("40.00"), fee);
    }

    @Test
    void shouldCalculateExtraFeeWhenDurationIs5Hours_VIP() {
        BigDecimal fee = billingService.calculateFee(5, SlotType.VIP);
        // 16.00 + (5-2) * 10.00 = 46.00
        assertEquals(new BigDecimal("46.00"), fee);
    }

    @Test
    void shouldCalculateExtraFeeWhenDurationIs5Hours_Large() {
        BigDecimal fee = billingService.calculateFee(5, SlotType.LARGE);
        // 16.00 + (5-2) * 9.00 = 43.00
        assertEquals(new BigDecimal("43.00"), fee);
    }
}