package com.example.parkinglot.controller;

import com.example.parkinglot.common.api.ApiResponse;
import com.example.parkinglot.common.exception.BusinessException;
import com.example.parkinglot.mapper.CloseTicketMapper;
import com.example.parkinglot.mapper.CreateTicketMapper;
import com.example.parkinglot.model.dto.EntryDTO;
import com.example.parkinglot.model.dto.ExitDTO;
import com.example.parkinglot.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final CreateTicketMapper createTicketMapper;
    private final CloseTicketMapper closeTicketMapper;

    public TicketController(TicketService ticketService,
                            CreateTicketMapper createTicketMapper,
                            CloseTicketMapper closeTicketMapper){
        this.ticketService = ticketService;
        this.createTicketMapper = createTicketMapper;
        this.closeTicketMapper = closeTicketMapper;
    }

    @PostMapping("/entry/{vehiclePlate}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EntryDTO> createEntryTicket(@PathVariable("vehiclePlate") String plate){
        return ticketService.createTicket(plate)
                .map(createTicketMapper::toDTO)
                .map(ApiResponse::success)
                .orElseThrow(() -> new BusinessException("入场失败！")) ;
    }

    @PostMapping("/exit/{vehiclePlate}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ExitDTO> closeTicket(@PathVariable("vehiclePlate") String plate){
        return ticketService.closeTicket(plate)
                .map(closeTicketMapper::toDTO)
                .map(ApiResponse::success)
                .orElseThrow(() -> new BusinessException("出场失败！"));
    }
}