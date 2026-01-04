package com.example.parkinglot.mapper;

import com.example.parkinglot.model.dto.EntryDTO;
import com.example.parkinglot.model.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class CreateTicketMapper {

    public EntryDTO toDTO(Ticket ticket){
        if(ticket ==null){
            return null;
        }
        return new EntryDTO()
                .setSlotNumber(ticket.getSlot().getSlotNumber())
                .setType(ticket.getSlotType())
                .setVehiclePlate(ticket.getVehicle().getVehiclePlate())
                .setEntryTime(ticket.getEntryTime())
                .setTicketStatus(ticket.getStatus());
    };
}