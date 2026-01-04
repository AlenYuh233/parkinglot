package com.example.parkinglot.service.impl;

import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.repository.ParkingSlotRepository;
import com.example.parkinglot.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void shouldReturnGlobalUsageStats() {

        when(parkingSlotRepository.count()).thenReturn(100L);
        when(parkingSlotRepository.countByStatus(SlotStatus.OCCUPIED)).thenReturn(25L);

        Map<String, Object> stats = reportService.getParkingSlotUsage();

        assertNotNull(stats);
        assertEquals(100L, stats.get("totalSlots"));
        assertEquals(25L, stats.get("occupiedSlots"));
        assertEquals("25.00%", stats.get("usageRatePercent"));

        verify(parkingSlotRepository).count();
        verify(parkingSlotRepository).countByStatus(SlotStatus.OCCUPIED);
    }

    @Test
    void shouldReturnUsageStatsByType() {

        SlotType type = SlotType.STANDARD;
        when(parkingSlotRepository.countByType(type)).thenReturn(50L);
        when(parkingSlotRepository.countByStatusAndType(SlotStatus.OCCUPIED, type)).thenReturn(10L);

        Map<String, Object> stats = reportService.getParkingSlotUsage(type);

        assertNotNull(stats);
        assertEquals(50L, stats.get("total STANDARD slots"));
        assertEquals(10L, stats.get("occupied STANDARD slots"));
        assertEquals("20.00%", stats.get("usageRatePercent"));

        verify(parkingSlotRepository).countByType(type);
        verify(parkingSlotRepository).countByStatusAndType(SlotStatus.OCCUPIED, type);
    }

    @Test
    void shouldReturnZeroUsageWhenTotalSlotsIsZero() {

        when(parkingSlotRepository.count()).thenReturn(0L);
        when(parkingSlotRepository.countByStatus(SlotStatus.OCCUPIED)).thenReturn(0L);

        Map<String, Object> stats = reportService.getParkingSlotUsage();

        assertEquals("0.00%", stats.get("usageRatePercent"));
    }

    @Test
    void shouldReturnTotalIncomeByDate() {

        LocalDate date = LocalDate.of(2025, 12, 25);
        BigDecimal expectedIncome = new BigDecimal("500.00");


        when(ticketRepository.sumTotalAmountByTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedIncome);

        BigDecimal actualIncome = reportService.getTotalIncomeByDate(date);

        assertEquals(expectedIncome, actualIncome);
        verify(ticketRepository).sumTotalAmountByTimeBetween(
                eq(date.atStartOfDay()),
                eq(date.plusDays(1).atStartOfDay())
        );
    }

    @Test
    void shouldReturnZeroIncomeWhenDateIsNull() {

        LocalDate today = LocalDate.now();
        when(ticketRepository.sumTotalAmountByTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);


        BigDecimal actualIncome = reportService.getTotalIncomeByDate(null);


        assertEquals(BigDecimal.ZERO, actualIncome);
        verify(ticketRepository).sumTotalAmountByTimeBetween(
                eq(today.atStartOfDay()),
                eq(today.plusDays(1).atStartOfDay())
        );
    }

    @Test
    void shouldReturnZeroIncomeWhenDatabaseReturnsNull() {

        LocalDate date = LocalDate.of(2025, 12, 25);
        when(ticketRepository.sumTotalAmountByTimeBetween(any(), any())).thenReturn(null);

        BigDecimal actualIncome = reportService.getTotalIncomeByDate(date);

        assertEquals(BigDecimal.ZERO, actualIncome);
    }
}