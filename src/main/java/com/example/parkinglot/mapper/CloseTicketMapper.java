package com.example.parkinglot.mapper;

import com.example.parkinglot.model.dto.ExitDTO;
import com.example.parkinglot.model.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class CloseTicketMapper {

    public ExitDTO toDTO(Ticket ticket){
        if(ticket == null){
            return null;
        }
        return new ExitDTO()
                .setVehiclePlate(ticket.getVehicle().getVehiclePlate())
                .setEntryTime(ticket.getEntryTime())
                .setExitTime(ticket.getExitTime())
                .setSlotNumber(ticket.getSlot().getSlotNumber())
                .setType(ticket.getSlotType())
                .setTicketStatus(ticket.getStatus())
                .setTotalAmount(ticket.getTotalAmount());
    }
}