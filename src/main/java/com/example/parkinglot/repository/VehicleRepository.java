package com.example.parkinglot.repository;

import com.example.parkinglot.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehiclePlate(String vehiclePlate);

    boolean existsByVehiclePlate(String vehiclePlate);
}