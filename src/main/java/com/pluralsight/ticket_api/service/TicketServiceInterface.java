package com.pluralsight.ticket_api.service;

import com.pluralsight.ticket_api.dto.TicketDto;
import com.pluralsight.ticket_api.dto.TicketFilteredDto;
import com.pluralsight.ticket_api.service.exception.AgentNotFoundException;
import com.pluralsight.ticket_api.service.exception.IllegalTicketStatusException;

import java.util.List;

public interface TicketServiceInterface {
    TicketDto saveTicket(TicketDto any);

    TicketDto assignTicket(Long id , Long agentId) throws IllegalTicketStatusException, AgentNotFoundException;

    TicketDto resolveTicket(Long id);

    TicketDto closeTicket(Long id);

    TicketDto updateTicket(Long any, TicketDto any1);

    TicketDto getTickets(Long ticketId);

    List<TicketDto> getAllTickets(TicketFilteredDto ticketFilteredDto);
}
