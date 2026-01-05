package com.example.parkinglot.service.impl;

import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.common.utils.TimeUtils;
import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.entity.Ticket;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.TicketStatus;
import com.example.parkinglot.repository.TicketRepository;
import com.example.parkinglot.service.BillingService;
import com.example.parkinglot.service.ParkingService;
import com.example.parkinglot.service.TicketService;
import com.example.parkinglot.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    public final ParkingService parkingService;
    public final VehicleService vehicleService;
    public final TicketRepository ticketRepository;
    public final BillingService billingService;

    public TicketServiceImpl(ParkingService parkingService,
                             TicketRepository ticketRepository,
                             VehicleService vehicleService,
                             BillingService billingService){
        this.parkingService = parkingService;
        this.vehicleService = vehicleService;
        this.ticketRepository = ticketRepository;
        this.billingService = billingService;
    }

    @Override
    @Transactional
    @Retryable(value = ObjectOptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public Optional<Ticket> createTicket(String vehiclePlate){

        log.info(">> 准备处理车辆进场: {}，开始检验: ", vehiclePlate);
        //判断车辆有没有注册
        Vehicle vehicle = vehicleService.findByVehiclePlate(vehiclePlate)
                .orElseThrow(() -> new BusinessException("ENTRY_FAILURE", "进场失败: 车辆 " + vehiclePlate + " 不存在， 请注册车辆！"));

        //判断停车场是否已经有该车牌对应的车辆
        Optional<Ticket> existingTicketOpt = ticketRepository.findByStatusAndVehicle(TicketStatus.ACTIVE, vehicle);
        existingTicketOpt.ifPresent(ticket -> {
            throw new BusinessException("ENTRY_FAILURE", "进场失败: 车辆 " + vehiclePlate + " 已经在停车场中！");
        });

        //判断还有没有空余车位
        ParkingSlot freeSlot = parkingService.findFirstFreeByType(vehicle.getType())
                .orElseThrow(() ->
                new BusinessException("ENTRY_FAILURE", "进场失败: 没有空闲的 " + vehicle.getType() + " 类型停车位"));

        log.info(">>查找到空闲车位: {}", freeSlot.getSlotNumber());
        parkingService.occupyFreeSlot(freeSlot.getId());

        LocalDateTime localDateTime = LocalDateTime.now();

        //新建票据，更新到数据库
        Ticket ticket = new Ticket()
                .setSlot(freeSlot)
                .setSlotType(freeSlot.getType())
                .setStatus(TicketStatus.ACTIVE)
                .setVehicle(vehicle)
                .setEntryTime(localDateTime);

        ticketRepository.save(ticket);
        log.info(">>新建票据，保存到数据库, 票据id: {}", ticket.getId());
        return Optional.of(ticket);
    }

    @Override
    @Transactional
    public Optional<Ticket> closeTicket(String vehiclePlate) {

        log.info(">> 准备处理车辆出场: {}，开始检验: ", vehiclePlate);
        Vehicle vehicle = vehicleService.findByVehiclePlate(vehiclePlate)
                .orElseThrow(() -> new BusinessException("EXIT_FAILURE", "出场失败: 车辆 " + vehiclePlate + " 不存在!"));

        Ticket existingTicket = ticketRepository.findByStatusAndVehicle(TicketStatus.ACTIVE, vehicle)
                .orElseThrow(() -> new BusinessException("EXIT_FAILURE", "出场失败: 车辆 " + vehiclePlate + " 不在停车场中!"));

        log.info(">>查询到对应票据id: {}", existingTicket.getId());
        parkingService.freeOccupiedSlot(existingTicket.getSlot().getId());

        LocalDateTime exitTime = LocalDateTime.now();

        long hours = TimeUtils.calculateParkingHours(existingTicket.getEntryTime(), exitTime);

        BigDecimal amount = billingService.calculateFee(hours, vehicle.getType());

        //更新票据数据，结算金额
        existingTicket.setExitTime(exitTime)
                .setStatus(TicketStatus.PAID)
                .setTotalAmount(amount);

        ticketRepository.save(existingTicket);
        log.info(">>更新票据数据，填写金额，保存到数据库, 票据id: {}", existingTicket.getId());

        return Optional.of(existingTicket);
    }
}