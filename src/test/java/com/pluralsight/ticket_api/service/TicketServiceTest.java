package com.pluralsight.ticket_api.service;

import com.pluralsight.ticket_api.dto.TicketDto;
import com.pluralsight.ticket_api.dto.TicketFilteredDto;
import com.pluralsight.ticket_api.model.Agent;
import com.pluralsight.ticket_api.model.Status;
import com.pluralsight.ticket_api.model.Ticket;
import com.pluralsight.ticket_api.repository.AgentRepository;
import com.pluralsight.ticket_api.repository.TicketRepository;
import com.pluralsight.ticket_api.service.exception.MissingDescriptionException;
import com.pluralsight.ticket_api.service.exception.TicketNotFoundException;
import com.pluralsight.ticket_api.service.impl.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    TicketService ticketService;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    AgentRepository agentRepository;

    @BeforeEach
    void setup() {
        ticketService = new TicketService(ticketRepository, agentRepository);
    }

    @Test
    void givenTicketDetail_WhenTicketIsCreated_ThenTicketSaved() {
        TicketDto ticketDto = new TicketDto(1L, "description", Status.CLOSED, null, null, null, null);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());

        ticketService.saveTicket(ticketDto);

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void givenTicketDetail_WhenTicketIsCreated_ThenTicketSavedAndDetailsAreCorrect() {
        TicketDto ticketDto = new TicketDto(1L, "description", Status.CLOSED, null, null, null, null);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket(1L, "description", Status.CLOSED, null, null, null, null));

        TicketDto ticket = ticketService.saveTicket(ticketDto);

        assertNotNull(ticket);
        assertEquals(Status.NEW, ticket.status());
        assertEquals(ticket.description(), ticketDto.description());
    }

    @Test
    void givenTicketDetailWithoutDescription_WhenTicketIsCreated_ThenMissingDescriptionExceptionIsRaised() {
        TicketDto ticketDto = new TicketDto(1L, null, Status.CLOSED, null, null, null, null);

        assertThrows(MissingDescriptionException.class, () -> ticketService.saveTicket(ticketDto));
    }

    @Test
    void givenNewTicket_whenAssigningAgent_thenStatusIsInProgress() {
        Long ticketId = 1L;
        Long agentId = 1L;
        String description = "description";
        Agent agent = new Agent(agentId, "Agent001");
        Ticket ticket = new Ticket(ticketId, description, Status.NEW, LocalDateTime.now(), LocalDateTime.now(), agent, "");
        Ticket ticketSaved = new Ticket(ticketId, description, Status.ASSIGNED, LocalDateTime.now(), LocalDateTime.now(), agent, "");

        when(ticketRepository.save(ticketSaved)).thenReturn(ticketSaved);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));

        TicketDto ticketDto = ticketService.assignTicket(ticketId, agentId);

        assertEquals(agentId, ticketDto.assignedAgent().getAgent_id());
        assertEquals(ticketId, ticketDto.id());
        assertEquals(Status.ASSIGNED, ticketDto.status());
    }

    @Test
    void givenTicketInProgress_whenResolving_thenStatusIsResolved() {
        Long ticketId = 1L;
        Long agentId = 1L;
        String description = "description";
        Agent agent = new Agent(agentId, "Agent001");
        LocalDateTime endDate = LocalDateTime.now();//TODO:mock using clock
        Ticket ticket = new Ticket(ticketId, description, Status.IN_PROGRESS, endDate, endDate, agent, "");
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        Ticket ticketSaved = new Ticket(ticketId, description, Status.RESOLVED, endDate, endDate, agent, "");
        when(ticketRepository.save(ticketSaved)).thenReturn(ticketSaved);

        TicketDto ticketDto = ticketService.resolveTicket(ticketId);

        assertEquals(Status.RESOLVED, ticketDto.status());
    }

    @Test
    void givenTicketResolved_whenCloseTheTicket_thenStatusIsClosed() {
        Long ticketId = 1L;
        Long agentId = 1L;
        String description = "description";
        Agent agent = new Agent(agentId, "Agent001");
        LocalDateTime endDate = LocalDateTime.now();//TODO:mock using clock
        Ticket ticket = new Ticket(ticketId, description, Status.IN_PROGRESS, endDate, endDate, agent, "");
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        Ticket ticketSaved = new Ticket(ticketId, description, Status.CLOSED, endDate, endDate, agent, "");
        when(ticketRepository.save(ticketSaved)).thenReturn(ticketSaved);

        TicketDto ticketDto = ticketService.closeTicket(ticketId);

        assertEquals(Status.CLOSED, ticketDto.status());
    }

    @Test
    void givenTicketId_whenUpdateTheTicket_thenTicketUpdated() {
        Long ticketId = 1L;
        Long agentId = 1L;
        String description = "description";
        Agent agent = new Agent(agentId, "Agent001");
        LocalDateTime endDate = LocalDateTime.now();//TODO:mock using clock
        Ticket ticket = new Ticket(ticketId, description, Status.IN_PROGRESS, endDate, endDate, agent, "");
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        String descriptionUpdated = "description Test";
        String resolutionSummury = "resolutionSummury";
        Agent agent002 = new Agent(2L, "Agent002");
        TicketDto ticketSaved = new TicketDto(ticketId, descriptionUpdated, Status.CLOSED, endDate, endDate, agent002, resolutionSummury);
        Ticket ticketSavedModel = new Ticket(ticketId, descriptionUpdated, Status.CLOSED, endDate, endDate, agent002, resolutionSummury);

        when(ticketRepository.save(ticketSavedModel)).thenReturn(ticketSavedModel);

        TicketDto ticketDto = ticketService.updateTicket(ticketId, ticketSaved);

        assertEquals(Status.CLOSED, ticketDto.status());
        assertEquals(descriptionUpdated, ticketDto.description());
        assertEquals(resolutionSummury, ticketDto.resolutionSummary());

    }

    @Test
    void givenTicketId_whenFetchTicketById_thenGetTicket() {
        Long ticketId = 1L;
        Long agentId = 1L;
        String description = "description";
        Agent agent = new Agent(agentId, "Agent001");
        LocalDateTime endDate = LocalDateTime.now();//TODO:mock using clock
        Ticket ticket = new Ticket(ticketId, description, Status.IN_PROGRESS, endDate, endDate, agent, "");
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        TicketDto ticketDto = ticketService.getTickets(ticketId);

        assertEquals(Status.IN_PROGRESS, ticketDto.status());
        assertEquals(description, ticketDto.description());
        assertEquals(agent.getAgent001(), ticketDto.assignedAgent().getAgent001());
    }

    @Test
    void givenTicketId_WhenTicketIsFetched_ThenMissingTicketExceptionIsRaised() {
        when(ticketRepository.findById(1L)).thenThrow(new TicketNotFoundException(""));
        assertThrows(TicketNotFoundException.class, () -> ticketService.getTickets(1L));
    }

    @Test
    void givenFilterCriteria_whenGettingTickets_thenReturnFiltredTickets(){
        TicketFilteredDto ticketFilteredDto = new TicketFilteredDto(List.of(Status.NEW),null,null,"");
        long agentId = 1L;
        long ticketId = 1L;
        String description = "Description";
        Agent agent = new Agent(agentId, "Agent001");
        LocalDateTime endDate = LocalDateTime.now();//TODO:mock using clock
        List<Ticket> filtredTickets = List.of(
                new Ticket(ticketId, description, Status.IN_PROGRESS, endDate, endDate, agent, ""),
                new Ticket(ticketId, description, Status.IN_PROGRESS, endDate, endDate, agent, ""));
        when(ticketRepository.findWithFilters(anyList(),any(),any(),any())).thenReturn(filtredTickets);

        List<TicketDto> retrievedTickets = ticketService.getAllTickets(ticketFilteredDto);

        assertEquals(2,retrievedTickets.size());
    }
}
