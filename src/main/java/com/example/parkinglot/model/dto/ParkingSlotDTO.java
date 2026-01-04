package com.example.parkinglot.model.dto;

import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotDTO {
    
    private Long id;
    private String slotNumber;
    private SlotType type;
    private SlotStatus status;
}

