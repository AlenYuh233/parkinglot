package com.example.parkinglot.mapper;

import com.example.parkinglot.model.dto.ParkingSlotDTO;
import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSlotMapperTest {

    private ParkingSlotMapper mapper;
    private ParkingSlot slot;

    @BeforeEach
    void setUp() {
        mapper = new ParkingSlotMapper();
        slot = new ParkingSlot()
                .setId(1L)
                .setSlotNumber("A001")
                .setType(SlotType.STANDARD)
                .setStatus(SlotStatus.FREE);
    }

    @Test
    void shouldReturnNullWhenSlotIsNull() {
        ParkingSlotDTO result = mapper.toDTO(null);
        assertNull(result);
    }

    @Test
    void shouldMapAllFieldsWhenSlotIsValid() {
        ParkingSlotDTO result = mapper.toDTO(slot);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("A001", result.getSlotNumber());
        assertEquals(SlotType.STANDARD, result.getType());
        assertEquals(SlotStatus.FREE, result.getStatus());
    }
}