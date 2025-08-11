package com.pluralsight.ticket_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pluralsight.ticket_api.constant.Constants;
import com.pluralsight.ticket_api.dto.TicketDto;
import com.pluralsight.ticket_api.dto.TicketFilteredDto;
import com.pluralsight.ticket_api.model.Agent;
import com.pluralsight.ticket_api.model.Status;
import com.pluralsight.ticket_api.service.TicketServiceInterface;
import com.pluralsight.ticket_api.service.exception.AgentNotFoundException;
import com.pluralsight.ticket_api.service.exception.IllegalTicketStatusException;
import com.pluralsight.ticket_api.service.exception.TicketDoneException;
import com.pluralsight.ticket_api.service.exception.TicketNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.pluralsight.ticket_api.constant.Constants.AGENT_NOT_FOUND;
import static com.pluralsight.ticket_api.constant.Constants.ONLY_NEW_TICKETS_CAN_BE_ASSIGNED_TO_AN_AGENT;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketsController.class)
public class TicketsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private TicketServiceInterface ticketServiceInterface;

    @Test
    void givenTicket_WhenTicketValid_thenIsCreated() throws Exception {
        String description = "this is the test";

        TicketDto ticketDto = new TicketDto(1L, description, Status.NEW, null, null, null, null);

        when(ticketServiceInterface.saveTicket(any(TicketDto.class))).thenReturn(ticketDto);

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(Status.NEW.name()))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    void givenTiketIdAndAgentId_WhenAssignTicketToAgent_ThenIsAssigned() throws Exception {
        String description = "this is the updated test";
        Agent agent = new Agent(1L, "Agent001");
        TicketDto ticketDto = new TicketDto(1L, description, Status.ASSIGNED, null, null, agent, null);

        when(ticketServiceInterface.assignTicket(any(Long.class), any(Long.class))).thenReturn(ticketDto);

        mockMvc.perform(put("/tickets/{id}/agent/{agent_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.status").value(Status.ASSIGNED.name()));
    }

    @Test
    void givenTiketIdAndAgentId_WhenAssignTicketToAgent_ThenIsAssignedException() throws Exception {
        String description = "this is the updated test";
        TicketDto ticketDto = new TicketDto(1L, description, Status.ASSIGNED, null, null,
                new Agent(1L, "Agent_001"), null);

        when(ticketServiceInterface.assignTicket(any(Long.class), any(Long.class))).thenThrow(new IllegalTicketStatusException(ONLY_NEW_TICKETS_CAN_BE_ASSIGNED_TO_AN_AGENT));

        mockMvc.perform(put("/tickets/{id}/agent/{agent_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ONLY_NEW_TICKETS_CAN_BE_ASSIGNED_TO_AN_AGENT));
    }

    @Test
    void givenTiketIdAndAgentId_WhenAssignTicketToAgent_ThenIsAssignedException2() throws Exception {
        String description = "this is the updated test";
        TicketDto ticketDto = new TicketDto(1L, description, Status.ASSIGNED, null, null, new Agent(1L, "Agent001")
                , null);

        when(ticketServiceInterface.assignTicket(any(Long.class), any(Long.class))).thenThrow(new AgentNotFoundException(AGENT_NOT_FOUND));

        mockMvc.perform(put("/tickets/{id}/agent/{agent_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(AGENT_NOT_FOUND));
    }

    @Test
    void givenTicketProgress_WhenRosolved_ThenStatusChangedToResolved() throws Exception {
        String description = "this is the test";
        TicketDto ticketDto = new TicketDto(1L, description, Status.RESOLVED, null, null, null, null);

        when(ticketServiceInterface.resolveTicket(any(Long.class))).thenReturn(ticketDto);

        mockMvc.perform(put("/tickets/{id}/resolve", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.RESOLVED.name()));
    }

    @Test
    void givenTicketProgress_WhenRosolved_ThenNotFound() throws Exception {
        String description = "this is the test";
        TicketDto ticketDto = new TicketDto(1L, description, Status.RESOLVED, null, null, null, null);

        when(ticketServiceInterface.resolveTicket(any(Long.class))).thenThrow(new TicketNotFoundException(Constants.TICKET_NOT_FOUND));

        mockMvc.perform(put("/tickets/{id}/resolve", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Constants.TICKET_NOT_FOUND));
    }

    @Test
    void givenTicketProgress_WhenRosolved_ThenBadStatus() throws Exception {
        String description = "this is the test";
        TicketDto ticketDto = new TicketDto(1L, description, Status.RESOLVED, null, null, null, null);

        when(ticketServiceInterface.resolveTicket(any(Long.class))).thenThrow(new IllegalTicketStatusException(Constants.TICKET_NOT_IN_PROGRESS_YET));

        mockMvc.perform(put("/tickets/{id}/resolve", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Constants.TICKET_NOT_IN_PROGRESS_YET));
    }

    @Test
    void givenTicketProgress_WhenRosolved_ThenStatusChangedToClosed() throws Exception {
        String description = "this is the test";
        TicketDto ticketDto = new TicketDto(1L, description, Status.CLOSED, null, null, null, null);

        when(ticketServiceInterface.closeTicket(any(Long.class))).thenReturn(ticketDto);

        mockMvc.perform(put("/tickets/{id}/close", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.CLOSED.name()));
    }

    @Test
    void givenTicketClosed_WhenOpened_ThenExceptionRaised() throws Exception {
        String description = "this is the test";
        TicketDto ticketDto = new TicketDto(1L, description, Status.CLOSED, null, null, null, null);

        when(ticketServiceInterface.closeTicket(any(Long.class))).thenThrow(new TicketDoneException("Ticket is closed"));

        mockMvc.perform(put("/tickets/{id}/close", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ticket is closed"));
    }

    @Test
    void givenTicketDetails_whenTicketUpdated_ThenDetailsUpdated() throws Exception {
        Long ticketId = 1L;
        String description = "this is the test";
        TicketDto ticketDto = new TicketDto(ticketId, description, Status.CLOSED, null, null, null, null);

        when(ticketServiceInterface.updateTicket(eq(ticketId), any(TicketDto.class))).thenReturn(ticketDto);

        mockMvc.perform(put("/tickets/{id}/update", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ticketDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.CLOSED.name()));

    }

    @Test
    void givenTicketId_whenFetchTicket_ThenGetTicketsDetails() throws Exception {
        Long ticketId = 1L;
        String description = "this is the test";
        TicketDto ticketDto = new TicketDto(ticketId, description, Status.CLOSED, null, null, null, null);

        when(ticketServiceInterface.getTickets(eq(ticketId))).thenReturn(ticketDto);

        mockMvc.perform(get("/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId))
                .andExpect(jsonPath("$.status").value(Status.CLOSED.name()));

    }

    @Test
    void given_whenFetchTickets_ThenGetAllTicketsDetails() throws Exception {
        String agentName = "Agent_001";
        TicketDto ticketDto = new TicketDto(1L, "this is the test", Status.CLOSED, LocalDateTime.now().minusDays(1), null,
                new Agent(1L, "Agent_001"), null);
        TicketDto ticketDto1 = new TicketDto(2L, "this is the test 2", Status.RESOLVED, LocalDateTime.now().minusDays(3), null,
                new Agent(1L, "Agent_001"), null);
        List<TicketDto> ticketDtoArrayList = List.of(ticketDto1, ticketDto);

        when(ticketServiceInterface.getAllTickets(any(TicketFilteredDto.class))).thenReturn(ticketDtoArrayList);

        mockMvc.perform(get("/tickets")
                        .param("status", "NEW,RESOLVED")
                        .param("startDate", LocalDateTime.now().minusDays(3).toString())
                        .param("endDate", LocalDateTime.now().minusDays(3).toString())
                        .param("assignedAgent", agentName)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(ticketDtoArrayList.size())));
    }


}
