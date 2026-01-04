package com.example.parkinglot.model.entity;

import com.example.parkinglot.model.enums.SlotType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(
        name = "Vehicle",
        uniqueConstraints = @UniqueConstraint(columnNames = "vehicle_plate")
)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true, nullable = false)
    private String vehiclePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SlotType type;

//    @Column(length = 50, nullable = false)
//    @Pattern(
//            regexp = "^[+]?[0-9\\s\\-\\(\\)]{8,50}$",
//            message = "电话号码格式无效（支持+、数字、空格、-、()，长度8-50）"
//    )
//    private String phone;
}