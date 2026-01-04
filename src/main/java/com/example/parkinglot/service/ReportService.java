package com.example.parkinglot.service;

import com.example.parkinglot.model.enums.SlotType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface ReportService {

    Map<String, Object> getParkingSlotUsage();

    Map<String, Object> getParkingSlotUsage(SlotType type);

    BigDecimal getTotalIncomeByDate(LocalDate date);
}