package com.example.parkinglot.mapper;

import com.example.parkinglot.model.dto.EntryDTO;
import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.entity.Ticket;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.model.enums.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreateTicketMapperTest {

    private CreateTicketMapper mapper;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        mapper = new CreateTicketMapper();

        Vehicle vehicle = new Vehicle()
                .setVehiclePlate("ABC-001")
                .setType(SlotType.STANDARD);

        ParkingSlot slot = new ParkingSlot()
                .setSlotNumber("A001")
                .setType(SlotType.STANDARD);

        ticket = new Ticket()
                .setVehicle(vehicle)
                .setSlot(slot)
                .setEntryTime(LocalDateTime.now())
                .setStatus(TicketStatus.ACTIVE);
    }

    @Test
    void shouldReturnNullWhenTicketIsNull() {
        EntryDTO result = mapper.toDTO(null);
        assertNull(result);
    }

    @Test
    void shouldMapAllFieldsWhenTicketIsValid() {
        EntryDTO result = mapper.toDTO(ticket);

        assertNotNull(result);
        assertEquals("ABC-001", result.getVehiclePlate());
        assertEquals("A001", result.getSlotNumber());
        assertEquals(ticket.getEntryTime(), result.getEntryTime());
        assertEquals(TicketStatus.ACTIVE, result.getTicketStatus());
    }
}