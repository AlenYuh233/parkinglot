package com.example.parkinglot.service;

import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;

import java.util.List;
import java.util.Optional;

public interface ParkingService{
    
    Optional<ParkingSlot> findFirstByStatus(SlotStatus status);

    List<ParkingSlot> findByStatus(SlotStatus status);

    Optional<ParkingSlot> findFirstFreeByType(SlotType type);

    void updateSlotStatus(Long slotId, SlotStatus newStatus);

    void occupyFreeSlot(Long slotId);

    void freeOccupiedSlot(Long slotId);
}