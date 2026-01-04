package com.example.parkinglot.service.impl;

import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.entity.Ticket;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.model.enums.TicketStatus;
import com.example.parkinglot.repository.TicketRepository;
import com.example.parkinglot.service.BillingService;
import com.example.parkinglot.service.ParkingService;
import com.example.parkinglot.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private ParkingService parkingService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private BillingService billingService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Vehicle testVehicle;
    private ParkingSlot testSlot;
    private Ticket testTicket;

    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle()
                .setId(1L)
                .setVehiclePlate("ABC-001")
                .setType(SlotType.STANDARD);

        testSlot = new ParkingSlot()
                .setId(1L)
                .setSlotNumber("A001")
                .setType(SlotType.STANDARD)
                .setStatus(SlotStatus.FREE);

        testTicket = new Ticket()
                .setId(1L)
                .setVehicle(testVehicle)
                .setSlot(testSlot)
                .setStatus(TicketStatus.ACTIVE)
                .setEntryTime(LocalDateTime.now());
    }

    @Test
    void shouldCreateTicketWhenAllConditionsMet() {
        when(vehicleService.findByVehiclePlate("ABC-001"))
                .thenReturn(Optional.of(testVehicle));
        when(ticketRepository.findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle))
                .thenReturn(Optional.empty());
        when(parkingService.findFirstFreeByType(SlotType.STANDARD))
                .thenReturn(Optional.of(testSlot));
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(testTicket);

        Optional<Ticket> result = ticketService.createTicket("ABC-001");

        assertTrue(result.isPresent());
        assertEquals(TicketStatus.ACTIVE, result.get().getStatus());
        verify(vehicleService).findByVehiclePlate("ABC-001");
        verify(ticketRepository).findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle);
        verify(parkingService).findFirstFreeByType(SlotType.STANDARD);
        verify(parkingService).occupyFreeSlot(1L);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void shouldThrowExceptionWhenCreateTicketVehicleNotExists() {
        when(vehicleService.findByVehiclePlate("ABC-999"))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ticketService.createTicket("ABC-999");
        });

        assertEquals("进场失败: 车辆 ABC-999 不存在， 请注册车辆！", exception.getMessage());
        verify(vehicleService).findByVehiclePlate("ABC-999");
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenVehicleAlreadyInParking() {
        when(vehicleService.findByVehiclePlate("ABC-001"))
                .thenReturn(Optional.of(testVehicle));
        when(ticketRepository.findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle))
                .thenReturn(Optional.of(testTicket));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ticketService.createTicket("ABC-001");
        });

        assertEquals("进场失败: 车辆 ABC-001 已经在停车场中！", exception.getMessage());
        verify(vehicleService).findByVehiclePlate("ABC-001");
        verify(ticketRepository).findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle);
        verify(parkingService, never()).findFirstFreeByType(any());
    }

    @Test
    void shouldThrowExceptionWhenNoFreeSlot() {
        when(vehicleService.findByVehiclePlate("ABC-001"))
                .thenReturn(Optional.of(testVehicle));
        when(ticketRepository.findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle))
                .thenReturn(Optional.empty());
        when(parkingService.findFirstFreeByType(SlotType.STANDARD))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ticketService.createTicket("ABC-001");
        });

        assertEquals("进场失败: 没有空闲的 STANDARD 类型停车位", exception.getMessage());
        verify(parkingService).findFirstFreeByType(SlotType.STANDARD);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void shouldCloseTicketWhenAllConditionsMet() {
        LocalDateTime entryTime = LocalDateTime.now().minusHours(3);
        testTicket.setEntryTime(entryTime);

        when(vehicleService.findByVehiclePlate("ABC-001"))
                .thenReturn(Optional.of(testVehicle));
        when(ticketRepository.findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle))
                .thenReturn(Optional.of(testTicket));
        when(billingService.calculateFee(3, SlotType.STANDARD))
                .thenReturn(new BigDecimal("24.00"));
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(testTicket);

        Optional<Ticket> result = ticketService.closeTicket("ABC-001");

        assertTrue(result.isPresent());
        assertEquals(TicketStatus.PAID, result.get().getStatus());
        assertNotNull(result.get().getExitTime());
        assertEquals(new BigDecimal("24.00"), result.get().getTotalAmount());
        verify(vehicleService).findByVehiclePlate("ABC-001");
        verify(ticketRepository).findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle);
        verify(parkingService).freeOccupiedSlot(1L);
        verify(billingService).calculateFee(3, SlotType.STANDARD);
        verify(ticketRepository).save(testTicket);
    }

    @Test
    void shouldThrowExceptionWhenCloseTicketVehicleNotExists() {
        when(vehicleService.findByVehiclePlate("ABC-999"))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ticketService.closeTicket("ABC-999");
        });

        assertEquals("出场失败: 车辆 ABC-999 不存在!", exception.getMessage());
        verify(vehicleService).findByVehiclePlate("ABC-999");
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCloseTicketVehicleNotInParking() {
        when(vehicleService.findByVehiclePlate("ABC-001"))
                .thenReturn(Optional.of(testVehicle));
        when(ticketRepository.findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ticketService.closeTicket("ABC-001");
        });

        assertEquals("出场失败: 车辆 ABC-001 不在停车场中!", exception.getMessage());
        verify(ticketRepository).findByStatusAndVehicle(TicketStatus.ACTIVE, testVehicle);
        verify(parkingService, never()).freeOccupiedSlot(any());
    }
}