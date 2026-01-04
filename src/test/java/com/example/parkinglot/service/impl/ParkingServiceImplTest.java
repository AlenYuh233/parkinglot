package com.example.parkinglot.service.impl;

import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.repository.ParkingSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceImplTest {

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @InjectMocks
    private ParkingServiceImpl parkingService;

    private ParkingSlot testSlot;

    @BeforeEach
    void setUp() {
        testSlot = new ParkingSlot()
                .setId(1L)
                .setSlotNumber("A001")
                .setType(SlotType.STANDARD)
                .setStatus(SlotStatus.FREE);
    }

    @Test
    void shouldReturnSlotWhenFindFirstByStatusExists() {
        when(parkingSlotRepository.findFirstByStatus(SlotStatus.FREE))
                .thenReturn(Optional.of(testSlot));

        Optional<ParkingSlot> result = parkingService.findFirstByStatus(SlotStatus.FREE);

        assertTrue(result.isPresent());
        assertEquals("A001", result.get().getSlotNumber());
        verify(parkingSlotRepository).findFirstByStatus(SlotStatus.FREE);
    }

    @Test
    void shouldReturnListWhenFindByStatusExists() {
        List<ParkingSlot> slots = List.of(testSlot);
        when(parkingSlotRepository.findByStatus(SlotStatus.FREE))
                .thenReturn(slots);

        List<ParkingSlot> result = parkingService.findByStatus(SlotStatus.FREE);

        assertEquals(1, result.size());
        assertEquals("A001", result.get(0).getSlotNumber());
        verify(parkingSlotRepository).findByStatus(SlotStatus.FREE);
    }

    @Test
    void shouldReturnSlotWhenFindFirstFreeByTypeExists() {
        when(parkingSlotRepository.findFirstByStatusAndType(SlotStatus.FREE, SlotType.STANDARD))
                .thenReturn(Optional.of(testSlot));

        Optional<ParkingSlot> result = parkingService.findFirstFreeByType(SlotType.STANDARD);

        assertTrue(result.isPresent());
        assertEquals("A001", result.get().getSlotNumber());
        verify(parkingSlotRepository).findFirstByStatusAndType(SlotStatus.FREE, SlotType.STANDARD);
    }

    @Test
    void shouldUpdateStatusWhenSlotExists() {
        when(parkingSlotRepository.findById(1L))
                .thenReturn(Optional.of(testSlot));
        when(parkingSlotRepository.save(any(ParkingSlot.class)))
                .thenReturn(testSlot);

        parkingService.updateSlotStatus(1L, SlotStatus.OCCUPIED);

        assertEquals(SlotStatus.OCCUPIED, testSlot.getStatus());
        verify(parkingSlotRepository).findById(1L);
        verify(parkingSlotRepository).save(testSlot);
    }

    @Test
    void shouldThrowExceptionWhenUpdateSlotNotExists() {
        when(parkingSlotRepository.findById(999L))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            parkingService.updateSlotStatus(999L, SlotStatus.OCCUPIED);
        });

        assertEquals("更新失败：车位不存在，无法更新状态", exception.getMessage());
        verify(parkingSlotRepository).findById(999L);
        verify(parkingSlotRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenMaintainToOccupied() {
        testSlot.setStatus(SlotStatus.MAINTAIN);
        when(parkingSlotRepository.findById(1L))
                .thenReturn(Optional.of(testSlot));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            parkingService.updateSlotStatus(1L, SlotStatus.OCCUPIED);
        });

        assertEquals("更新失败：维护中的车位无法改为占用状态", exception.getMessage());
        verify(parkingSlotRepository).findById(1L);
        verify(parkingSlotRepository, never()).save(any());
    }

    @Test
    void shouldCallUpdateSlotStatusWhenOccupyFreeSlot() {
        when(parkingSlotRepository.findById(1L))
                .thenReturn(Optional.of(testSlot));
        when(parkingSlotRepository.save(any(ParkingSlot.class)))
                .thenReturn(testSlot);

        parkingService.occupyFreeSlot(1L);

        assertEquals(SlotStatus.OCCUPIED, testSlot.getStatus());
        verify(parkingSlotRepository).findById(1L);
        verify(parkingSlotRepository).save(testSlot);
    }

    @Test
    void shouldCallUpdateSlotStatusWhenFreeOccupiedSlot() {
        testSlot.setStatus(SlotStatus.OCCUPIED);
        when(parkingSlotRepository.findById(1L))
                .thenReturn(Optional.of(testSlot));
        when(parkingSlotRepository.save(any(ParkingSlot.class)))
                .thenReturn(testSlot);

        parkingService.freeOccupiedSlot(1L);

        assertEquals(SlotStatus.FREE, testSlot.getStatus());
        verify(parkingSlotRepository).findById(1L);
        verify(parkingSlotRepository).save(testSlot);
    }
}