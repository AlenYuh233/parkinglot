package com.example.parkinglot.service;

import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.SlotType;

import java.util.Optional;

public interface VehicleService {

    Optional<Vehicle> findByVehiclePlate(String vehiclePlate);

    Vehicle addVehicle(String vehiclePlate, SlotType type);
}