package com.example.parkinglot.controller;

import com.example.parkinglot.common.api.ApiResponse;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    // 合并接口：支持全局查询（不传type）或按类型查询（传type）
    @GetMapping(value = {"/usage", "/usage/{type}"})
    public ApiResponse<Map<String, Object>> getUsageRate(@PathVariable(required = false) SlotType type) {
        Map<String, Object> result = (type == null)
                ? reportService.getParkingSlotUsage()
                : reportService.getParkingSlotUsage(type);
        return ApiResponse.success(result);
    }

    @GetMapping("/income/{date}")
    public ApiResponse<BigDecimal> getIncomeByDate(@PathVariable("date")LocalDate date){
        return ApiResponse.success(reportService.getTotalIncomeByDate(date));
    }
}