package com.example.parkinglot.controller;

import com.example.parkinglot.common.api.ApiResponse;
import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.mapper.ParkingSlotMapper;
import com.example.parkinglot.model.dto.ParkingSlotDTO;
import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.service.ParkingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/parking-slots")
public class ParkingSlotController {

    private final ParkingService parkingService;
    private final ParkingSlotMapper parkingSlotMapper;

    public ParkingSlotController(ParkingService parkingService, ParkingSlotMapper parkingSlotMapper){
        this.parkingService = parkingService;
        this.parkingSlotMapper = parkingSlotMapper;
    }

    @GetMapping("/status")
    public ApiResponse<ParkingSlotDTO> getFirstByStatus(@RequestParam SlotStatus status) {
        return parkingService.findFirstByStatus(status)
                .map(parkingSlotMapper::toDTO)
                .map(ApiResponse::success)
                .orElseThrow(() -> new BusinessException("查询失败！"));
    }

    @GetMapping("/status-list")
    public ApiResponse<List<ParkingSlotDTO>> getListByStatus(@RequestParam SlotStatus status){
         List<ParkingSlotDTO> listByStatus = parkingService.findByStatus(status).stream()
                .map(parkingSlotMapper::toDTO)
                .toList();
         return ApiResponse.success(listByStatus);
    }

    
}