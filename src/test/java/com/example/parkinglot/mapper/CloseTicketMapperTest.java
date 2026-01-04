package com.example.parkinglot.mapper;

import com.example.parkinglot.model.dto.ExitDTO;
import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.entity.Ticket;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.model.enums.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CloseTicketMapperTest {

    private CloseTicketMapper mapper;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        mapper = new CloseTicketMapper();

        Vehicle vehicle = new Vehicle()
                .setVehiclePlate("ABC-001")
                .setType(SlotType.STANDARD);

        ParkingSlot slot = new ParkingSlot()
                .setSlotNumber("A001")
                .setType(SlotType.STANDARD);

        ticket = new Ticket()
                .setVehicle(vehicle)
                .setSlot(slot)
                .setEntryTime(LocalDateTime.now().minusHours(3))
                .setExitTime(LocalDateTime.now())
                .setStatus(TicketStatus.PAID)
                .setTotalAmount(new BigDecimal("24.00"));
    }

    @Test
    void shouldReturnNullWhenTicketIsNull() {
        ExitDTO result = mapper.toDTO(null);
        assertNull(result);
    }

    @Test
    void shouldMapAllFieldsWhenTicketIsValid() {
        ExitDTO result = mapper.toDTO(ticket);

        assertNotNull(result);
        assertEquals("ABC-001", result.getVehiclePlate());
        assertEquals("A001", result.getSlotNumber());
        assertEquals(ticket.getEntryTime(), result.getEntryTime());
        assertEquals(ticket.getExitTime(), result.getExitTime());
        assertEquals(TicketStatus.PAID, result.getTicketStatus());
        assertEquals(new BigDecimal("24.00"), result.getTotalAmount());
    }
}