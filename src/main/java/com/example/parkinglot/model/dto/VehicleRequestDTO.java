package com.example.parkinglot.model.dto;

import com.example.parkinglot.model.enums.SlotType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequestDTO {
    private String vehiclePlate;
    private SlotType type;
}