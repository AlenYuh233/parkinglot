package com.example.parkinglot.mapper;

import com.example.parkinglot.model.dto.ParkingSlotDTO;
import com.example.parkinglot.model.entity.ParkingSlot;
import org.springframework.stereotype.Component;

@Component
public class ParkingSlotMapper {
    
    public ParkingSlotDTO toDTO(ParkingSlot slot) {
        if (slot == null) {
            return null;
        }
        return new ParkingSlotDTO(
                slot.getId(),
                slot.getSlotNumber(),
                slot.getType(),
                slot.getStatus()
        );
    }
}

