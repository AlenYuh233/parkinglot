package com.example.parkinglot.controller;

import com.example.parkinglot.common.api.ApiResponse;
import com.example.parkinglot.model.dto.VehicleRequestDTO;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.service.VehicleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }


    @PostMapping("/addVehicle")
    public ApiResponse<Vehicle> addVehicle(@RequestBody VehicleRequestDTO vehicleRequestDTO){

        Vehicle newAddedVehicle =  vehicleService.addVehicle(vehicleRequestDTO.getVehiclePlate(), vehicleRequestDTO.getType());
        return ApiResponse.success(newAddedVehicle);
    }

}