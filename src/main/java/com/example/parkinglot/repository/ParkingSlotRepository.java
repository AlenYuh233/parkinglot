package com.example.parkinglot.repository;

import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long>{

    Optional<ParkingSlot> findFirstByStatus(SlotStatus status);

    List<ParkingSlot> findByStatus(SlotStatus status);

    Optional<ParkingSlot> findFirstByStatusAndType(SlotStatus status, SlotType type);

    long countByStatusAndType(SlotStatus status, SlotType type);

    long countByType(SlotType type);

    long countByStatus(SlotStatus status);
}