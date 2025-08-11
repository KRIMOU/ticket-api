package com.pluralsight.ticket_api.controller;

import com.pluralsight.ticket_api.dto.TicketDto;
import com.pluralsight.ticket_api.dto.TicketFilteredDto;
import com.pluralsight.ticket_api.service.TicketServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketsController {

    @Autowired
    private TicketServiceInterface ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto){
        TicketDto ticketDto1=ticketService.saveTicket(ticketDto);
        return new ResponseEntity<TicketDto>(ticketDto1, HttpStatus.CREATED);
    }


    @PutMapping("/{id}/agent/{agent_id}")
    public ResponseEntity<TicketDto> assignTicketToAgent(@PathVariable Long id, @PathVariable Long agent_id){
        TicketDto ticketDto1 = ticketService.assignTicket(id,agent_id);
        return new ResponseEntity<TicketDto>(ticketDto1, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<TicketDto> resolveTicket(@PathVariable Long id){
        TicketDto ticketDto1 = ticketService.resolveTicket(id);
        return new ResponseEntity<TicketDto>(ticketDto1, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<TicketDto> closeTicket(@PathVariable Long id){
        TicketDto ticketDto1 = ticketService.closeTicket(id);
        return new ResponseEntity<>(ticketDto1, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable long id , TicketDto ticketDto){
        TicketDto ticketDto1 = ticketService.updateTicket(id,ticketDto);
        return new ResponseEntity<TicketDto>(ticketDto1, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable long id ){
        TicketDto ticketDto1 = ticketService.getTickets(id);
        return new ResponseEntity<>(ticketDto1, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getTickets(TicketFilteredDto ticketFilteredDto){
        List<TicketDto> ticketDto1 = ticketService.getAllTickets(ticketFilteredDto);
        return new ResponseEntity<>(ticketDto1, HttpStatus.OK);
    }
}
