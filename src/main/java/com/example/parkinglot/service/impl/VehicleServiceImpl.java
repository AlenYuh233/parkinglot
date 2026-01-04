package com.example.parkinglot.service.impl;

import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.repository.VehicleRepository;
import com.example.parkinglot.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> findByVehiclePlate(String vehiclePlate) {
        return vehicleRepository.findByVehiclePlate(vehiclePlate);
    }

    @Override
    @Transactional
    public Vehicle addVehicle(String vehiclePlate, SlotType type){

        if(vehicleRepository.existsByVehiclePlate(vehiclePlate)){
            throw new BusinessException("REGISTRATION_FAILURE", "注册失败：车辆已经存在！");
        }
        log.info(">>注册车辆,Plate =  {} , Type = {}, 保存到数据库", vehiclePlate, type);
        Vehicle vehicle = new Vehicle()
                .setVehiclePlate(vehiclePlate)
                .setType(type);

        return vehicleRepository.save(vehicle);
    }
}