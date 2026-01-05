package com.example.parkinglot.repository;

import com.example.parkinglot.model.entity.Ticket;
import com.example.parkinglot.model.entity.Vehicle;
import com.example.parkinglot.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    //使用join fetch，一次性读取数据到内存，n+1问题
    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.vehicle " +
            "JOIN FETCH t.slot " +
            "WHERE t.status = :status AND t.vehicle = :vehicle")
    Optional<Ticket> findByStatusAndVehicle(TicketStatus status, Vehicle vehicle);

    @Query("SELECT SUM(t.totalAmount) " +
            "FROM Ticket t " +
            "WHERE t.status = 'PAID' " +
            "AND t.exitTime >= :start " +
            "AND t.exitTime < :end")
    BigDecimal sumTotalAmountByTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}