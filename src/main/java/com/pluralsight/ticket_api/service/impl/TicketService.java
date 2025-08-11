package com.pluralsight.ticket_api.service.impl;

import com.pluralsight.ticket_api.constant.Constants;
import com.pluralsight.ticket_api.dto.TicketDto;
import com.pluralsight.ticket_api.dto.TicketFilteredDto;
import com.pluralsight.ticket_api.model.Agent;
import com.pluralsight.ticket_api.model.Status;
import com.pluralsight.ticket_api.model.Ticket;
import com.pluralsight.ticket_api.repository.AgentRepository;
import com.pluralsight.ticket_api.repository.TicketRepository;
import com.pluralsight.ticket_api.service.TicketServiceInterface;
import com.pluralsight.ticket_api.service.exception.AgentNotFoundException;
import com.pluralsight.ticket_api.service.exception.IllegalTicketStatusException;
import com.pluralsight.ticket_api.service.exception.MissingDescriptionException;
import com.pluralsight.ticket_api.service.exception.TicketNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.pluralsight.ticket_api.constant.Constants.TICKET_NOT_FOUND;

@Service
public class TicketService implements TicketServiceInterface {

    private final TicketRepository ticketRepository;
    private final AgentRepository agentRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,AgentRepository agentRepository){
        this.ticketRepository = ticketRepository;
        this.agentRepository = agentRepository;
    }

    public TicketDto saveTicket(TicketDto ticketDto) {
        System.out.println("Test"+ticketDto.description());
        if(ticketDto.description()==null || ticketDto.description().isEmpty()){
            throw new MissingDescriptionException(Constants.DESCRIPTION_IS_MISSING);
        }
        Ticket ticket = new Ticket(ticketDto.id(),
                ticketDto.description(),
                Status.NEW,
                LocalDateTime.now(),null,null,null);
        Ticket ticketSaved =ticketRepository.save(ticket);
        return new TicketDto(ticketSaved.getId(),
               ticketSaved.getDescription(),
                Status.NEW,
                ticketSaved.getCreatedDateTime(),
                ticketSaved.getClosedDateTime(),
                ticketSaved.getAssignedAgent(),
                ticketSaved.getResolutionSummary());
   }

    @Override
    public TicketDto assignTicket(Long id, Long agentId) throws IllegalTicketStatusException, AgentNotFoundException {
        Optional<Agent> agent = agentRepository.findById(agentId);
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.get().setStatus(Status.ASSIGNED);
        ticket.get().setAssignedAgent(agent.get());

        Ticket ticketUpdated=ticketRepository.save(ticket.get());
        return new TicketDto(ticketUpdated.getId(),
                ticketUpdated.getDescription(),
                ticketUpdated.getStatus(),
                ticketUpdated.getCreatedDateTime(),
                ticketUpdated.getClosedDateTime(),
                ticketUpdated.getAssignedAgent(),
                ticketUpdated.getResolutionSummary());
    }

    @Override
    public TicketDto resolveTicket(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.get().setStatus(Status.RESOLVED);

        Ticket ticketUpdated=ticketRepository.save(ticket.get());
        return new TicketDto(ticketUpdated.getId(),
                ticketUpdated.getDescription(),
                ticketUpdated.getStatus(),
                ticketUpdated.getCreatedDateTime(),
                ticketUpdated.getClosedDateTime(),
                ticketUpdated.getAssignedAgent(),
                ticketUpdated.getResolutionSummary());
    }

    @Override
    public TicketDto closeTicket(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.get().setStatus(Status.CLOSED);

        Ticket ticketUpdated=ticketRepository.save(ticket.get());
        return new TicketDto(ticketUpdated.getId(),
                ticketUpdated.getDescription(),
                ticketUpdated.getStatus(),
                ticketUpdated.getCreatedDateTime(),
                ticketUpdated.getClosedDateTime(),
                ticketUpdated.getAssignedAgent(),
                ticketUpdated.getResolutionSummary());
    }

    @Override
    public TicketDto updateTicket(Long any, TicketDto any1) {
        Optional<Ticket> ticket = ticketRepository.findById(any);
        ticket.get().setStatus(any1.status());
        ticket.get().setDescription(any1.description());
        ticket.get().setResolutionSummary(any1.resolutionSummary());
        ticket.get().setAssignedAgent(any1.assignedAgent());
        Ticket ticketUpdated=ticketRepository.save(ticket.get());
        return new TicketDto(ticketUpdated.getId(),
                ticketUpdated.getDescription(),
                ticketUpdated.getStatus(),
                ticketUpdated.getCreatedDateTime(),
                ticketUpdated.getClosedDateTime(),
                ticketUpdated.getAssignedAgent(),
                ticketUpdated.getResolutionSummary());
    }

    @Override
    public TicketDto getTickets(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()->new TicketNotFoundException(TICKET_NOT_FOUND));
        return new TicketDto(ticket.getId(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getCreatedDateTime(),
                ticket.getClosedDateTime(),
                ticket.getAssignedAgent(),
                ticket.getResolutionSummary());
    }

    @Override
    public List<TicketDto> getAllTickets(TicketFilteredDto ticketFilteredDto) {
        List<Ticket> filtredTickets = ticketRepository.findWithFilters(ticketFilteredDto.statusList(),ticketFilteredDto.startDataTime(),ticketFilteredDto.endDataTime(),ticketFilteredDto.assignedAgent());
        return filtredTickets.stream().map((ticket)->new TicketDto(ticket.getId(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getCreatedDateTime(),
                ticket.getClosedDateTime(),
                ticket.getAssignedAgent(),
                ticket.getResolutionSummary())).toList();
    }
}
