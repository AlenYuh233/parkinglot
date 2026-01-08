package com.example.parkinglot.model.entity;

import com.example.parkinglot.model.enums.SlotStatus;
import com.example.parkinglot.model.enums.SlotType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "parking_slots", indexes = {
    @Index(name = "idx_slot_status_type", columnList = "status, type"),
    @Index(name = "idx_slot_status", columnList = "status")
})
public class ParkingSlot{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String slotNumber;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotType type;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    @Version
    private Integer version;

}