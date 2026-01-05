package com.example.parkinglot.service.impl;

import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.repository.ParkingSlotRepository;
import com.example.parkinglot.repository.TicketRepository;
import com.example.parkinglot.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final TicketRepository ticketRepository;

    public ReportServiceImpl(ParkingSlotRepository parkingSlotRepository, TicketRepository ticketRepository){
        this.parkingSlotRepository = parkingSlotRepository;
        this.ticketRepository = ticketRepository;
    }


    @Override
    public Map<String, Object> getParkingSlotUsage() {
        long totalSlots = parkingSlotRepository.count();
        long occupiedSlots = parkingSlotRepository.countByStatus(SlotStatus.OCCUPIED);
        double usageRate = 0.0;

        if (totalSlots > 0) {
            usageRate = (double) occupiedSlots / totalSlots;
        }

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalSlots", totalSlots);
        stats.put("occupiedSlots", occupiedSlots);
        stats.put("usageRatePercent", String.format("%.2f%%", usageRate * 100));

        log.info(">> 正在统计车位使用率: {}/{} = {}", occupiedSlots, totalSlots, usageRate);
        return stats;
    }

    @Override
    public Map<String, Object> getParkingSlotUsage(SlotType type){
        long totalAmountByType = parkingSlotRepository.countByType(type);
        long occupiedByType = parkingSlotRepository.countByStatusAndType(SlotStatus.OCCUPIED, type);
        double usageRate = 0.0;

        if (totalAmountByType > 0) {
            usageRate = (double) occupiedByType / totalAmountByType;
        }

        Map<String, Object> stats = new HashMap<>();

        stats.put(String.format("total %s slots", type), totalAmountByType);
        stats.put(String.format("occupied %s slots", type), occupiedByType);
        stats.put("usageRatePercent", String.format("%.2f%%", usageRate * 100));

        log.info(">> 正在统计 {} 车位使用率: {}/{} = {}", type, occupiedByType, totalAmountByType, usageRate);
        return  stats;
    }

    @Override
    public BigDecimal getTotalIncomeByDate(LocalDate date) {
        if(date == null){
            date = LocalDate.now();
        }

        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.plusDays(1).atStartOfDay();

        BigDecimal totalIncome = ticketRepository.sumTotalAmountByTimeBetween(startTime, endTime);

        log.info("正在计算 {} 日的总收入: {}", date, totalIncome);

        return totalIncome != null ? totalIncome : BigDecimal.ZERO;
    }
}