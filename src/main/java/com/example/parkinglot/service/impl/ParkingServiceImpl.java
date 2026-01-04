package com.example.parkinglot.service.impl;

import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.model.entity.ParkingSlot;
import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import com.example.parkinglot.repository.ParkingSlotRepository;
import com.example.parkinglot.service.ParkingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ParkingServiceImpl implements ParkingService{

    private final ParkingSlotRepository parkingSlotRepository;

    public ParkingServiceImpl(ParkingSlotRepository parkingSlotRepository){
        this.parkingSlotRepository = parkingSlotRepository;
    }

    @Override
    public Optional<ParkingSlot> findFirstByStatus(SlotStatus status){
        return parkingSlotRepository.findFirstByStatus(status);
    }

    @Override
    public List<ParkingSlot> findByStatus(SlotStatus status){
        return parkingSlotRepository.findByStatus(status);
    }

    @Override
    public Optional<ParkingSlot> findFirstFreeByType(SlotType type){
        return parkingSlotRepository.findFirstByStatusAndType(SlotStatus.FREE, type);
    }

    @Override
    @Transactional
    public void updateSlotStatus(Long slotId, SlotStatus newStatus) {

        ParkingSlot slot = parkingSlotRepository.findById(slotId)
                .orElseThrow(() -> new BusinessException("UPDATE_FAILURE", "更新失败：车位不存在，无法更新状态"));

        if (SlotStatus.MAINTAIN.equals(slot.getStatus()) && SlotStatus.OCCUPIED.equals(newStatus)) {
            throw new BusinessException("UPDATE_FAILURE", "更新失败：维护中的车位无法改为占用状态");
        }
        log.info(">>更新车位{}状态为: {}", slot.getSlotNumber(), newStatus);
        slot.setStatus(newStatus);
        parkingSlotRepository.save(slot);
    }

    @Override
    public void occupyFreeSlot(Long slotId){
        updateSlotStatus(slotId, SlotStatus.OCCUPIED);
    }

    @Override
    public void freeOccupiedSlot(Long slotId){
        updateSlotStatus(slotId, SlotStatus.FREE);
    }
}