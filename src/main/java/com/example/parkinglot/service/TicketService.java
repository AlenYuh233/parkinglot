package com.example.parkinglot.service;

import com.example.parkinglot.model.entity.Ticket;

import java.util.Optional;

public interface TicketService {
    //entry
    Optional<Ticket> createTicket(String vehiclePlate);

    //exit
    Optional<Ticket> closeTicket(String vehiclePlate);
}