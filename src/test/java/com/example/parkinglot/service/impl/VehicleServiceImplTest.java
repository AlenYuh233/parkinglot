package com.example.parkinglot.service.impl;

import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle()
                .setId(1L)
                .setVehiclePlate("ABC-001")
                .setType(SlotType.STANDARD);
    }

    @Test
    void shouldReturnVehicleWhenExists() {
        when(vehicleRepository.findByVehiclePlate("ABC-001"))
                .thenReturn(Optional.of(testVehicle));

        Optional<Vehicle> result = vehicleService.findByVehiclePlate("ABC-001");

        assertTrue(result.isPresent());
        assertEquals("ABC-001", result.get().getVehiclePlate());
        verify(vehicleRepository).findByVehiclePlate("ABC-001");
    }

    @Test
    void shouldReturnEmptyWhenNotExists() {
        when(vehicleRepository.findByVehiclePlate("ABC-999"))
                .thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleService.findByVehiclePlate("ABC-999");

        assertFalse(result.isPresent());
        verify(vehicleRepository).findByVehiclePlate("ABC-999");
    }

    @Test
    void shouldSaveVehicleWhenNotExists() {
        when(vehicleRepository.existsByVehiclePlate("ABC-001"))
                .thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(testVehicle);

        Vehicle result = vehicleService.addVehicle("ABC-001", SlotType.STANDARD);

        assertNotNull(result);
        assertEquals("ABC-001", result.getVehiclePlate());
        assertEquals(SlotType.STANDARD, result.getType());
        verify(vehicleRepository).existsByVehiclePlate("ABC-001");
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void shouldThrowExceptionWhenVehicleExists() {
        when(vehicleRepository.existsByVehiclePlate("ABC-001"))
                .thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.addVehicle("ABC-001", SlotType.STANDARD);
        });

        assertEquals("注册失败：车辆已经存在！", exception.getMessage());
        verify(vehicleRepository).existsByVehiclePlate("ABC-001");
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
}