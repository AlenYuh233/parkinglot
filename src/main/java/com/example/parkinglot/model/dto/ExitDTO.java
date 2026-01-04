package com.example.parkinglot.model.dto;

import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExitDTO{
    private String vehiclePlate;
    private String slotNumber;
    private SlotType type;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private TicketStatus ticketStatus;
    private BigDecimal totalAmount;
}