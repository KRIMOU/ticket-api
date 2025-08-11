package com.pluralsight.ticket_api;

import com.pluralsight.ticket_api.dto.TicketDto;
import com.pluralsight.ticket_api.model.Agent;
import com.pluralsight.ticket_api.model.Status;
import com.pluralsight.ticket_api.model.Ticket;
import com.pluralsight.ticket_api.repository.AgentRepository;
import com.pluralsight.ticket_api.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/integrationTestData.sql"})
@Tag("integration")
public class TicketApiApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AgentRepository agentRepository;

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
        agentRepository.deleteAll();
    }

    @Test
    void createTicket_Successful() {
        TicketDto ticketDto = new TicketDto(null, "Sample Ticket", null, null, null, null, null);

        webTestClient.post().uri("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TicketDto.class)
                .value(ticketDtoResponse -> {
                    assertNotNull(ticketDtoResponse.id());
                    assertEquals("Sample Ticket", ticketDtoResponse.description());
                    assertEquals(Status.NEW, ticketDtoResponse.status());
                });
    }

    @Test
    void createTicket_MissingDescription() {
        TicketDto ticketDto = new TicketDto(null, null, null, null, null, null, null);

        webTestClient.post().uri("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void assignAgentToTicket_Successful() {
        // Given a ticket in "NEW" status
        webTestClient.put().uri("/tickets/100/agent/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDtoResponse -> {
                    assertEquals(Status.IN_PROGRESS, ticketDtoResponse.status());
                    assertEquals("Agent001", ticketDtoResponse.assignedAgent());
                });
    }

    @Test
    void assignAgentToTicket_AlreadyInProgress() {
        // Given a ticket in "IN_PROGRESS" status
        webTestClient.put().uri("/tickets/101/agent/1")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void resolveTicket_Successful() {
        // Given a ticket in "IN_PROGRESS" status
        webTestClient.put().uri("/tickets/101/resolve")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDtoResponse -> {
                    assertEquals(Status.RESOLVED, ticketDtoResponse.status());
                });
    }

    @Test
    void resolveTicket_InvalidState() {
        // Given a ticket in "NEW" status
        webTestClient.put().uri("/tickets/100/resolve")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void closeTicket_Successful() {
        // Given a ticket in "RESOLVED" status with a resolution summary
        webTestClient.put().uri("/tickets/103/close")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDtoResponse -> {
                    assertEquals(Status.CLOSED, ticketDtoResponse.status());
                });
    }

    @Test
    void closeTicket_MissingResolutionSummary() {
        // Given a ticket in "RESOLVED" status without a resolution summary
        webTestClient.put().uri("/tickets/102/close")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void closeTicket_InvalidState() {
        // Given a ticket in "NEW" status
        webTestClient.put().uri("/tickets/100/close")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateTicket_Successful() {
        String description = "Updated description";
        // Given a ticket not closed
        TicketDto ticketDto = new TicketDto(100L, description, null, null, null, null, null);

        webTestClient.put().uri("/tickets/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDtoResponse -> {
                    assertEquals(description, ticketDtoResponse.description());
                });
    }

    @Test
    void updateTicket_InvalidState() {
        // Given a closed ticket and trying to change its state in a way that's not allowed by business rules
        TicketDto ticketDto = new TicketDto(104L, null, null, null, null, null, "Resolution summary");  // Trying to move directly to CLOSED from NEW

        webTestClient.put().uri("/tickets/104")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getTicketById_Successful() {
        // Given a ticket with an assigned agent
        Long id = 101L;
        webTestClient.get().uri("/tickets/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDtoResponse -> {
                    assertEquals(id, ticketDtoResponse.id().longValue());
                    assertNotNull(ticketDtoResponse.assignedAgent());
                });
    }

    @Test
    void getTicketById_NonExistent() {
        // Given a non-existent ticket
        long id = 99L;
        webTestClient.get().uri("/tickets/" + id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getTickets_FilterByStatus() {
        // Filtering tickets with status "CLOSED"
        Status status = Status.CLOSED;
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/tickets")
                        .queryParam("status", status.name())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketDto.class)
                .hasSize(1)
                .consumeWith(response -> {
                    for (TicketDto ticketDto : response.getResponseBody()) {
                        assertEquals(status, ticketDto.status());
                    }
                });
    }

    @Test
    void combinedOperationsTest() {
        // Create a ticket
        TicketDto newTicketDto = new TicketDto(null, "Combined op ticket", Status.NEW, null, null, null, null);
        Long createdTicketId = webTestClient.post().uri("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newTicketDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TicketDto.class)
                .returnResult()
                .getResponseBody()
                .id();

        // Assign an agent
        webTestClient.put().uri("/tickets/" + createdTicketId + "/agent/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDto -> assertEquals(Status.IN_PROGRESS, ticketDto.status()));

        // Resolve ticket
        webTestClient.put().uri("/tickets/" + createdTicketId + "/resolve")
                .exchange()
                .expectStatus().isOk();

        // Update ticket with resolution summary
        TicketDto resolutionDto = new TicketDto(null, null, null, null, null, null, "Issue fixed.");
        webTestClient.put().uri("/tickets/" + createdTicketId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resolutionDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDto -> assertNotNull(ticketDto.resolutionSummary()));

        // Close ticket
        webTestClient.put().uri("/tickets/" + createdTicketId + "/close")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resolutionDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDto -> assertEquals(Status.CLOSED, ticketDto.status()));
    }

    @Test
    void assignAgent_DatabaseIntegrity() {
        // Given an existing ticket (id=100) and agent (id=1)
        Long ticketId = 100L;
        Agent agent = agentRepository.findById(1L).orElseThrow();

        // When we assign the agent to the ticket
        webTestClient.put().uri("/tickets/" + ticketId + "/agent/" + agent.getAgent_id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketDto.class)
                .value(ticketDto -> {
                    assertEquals(ticketId, ticketDto.id());
                    assertEquals(agent.getAgent001(), ticketDto.assignedAgent());
                });

        // Now, verify that the database has set the foreign key correctly
        // Fetch the ticket directly from the repository
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        assertEquals(agent.getAgent_id(), ticket.getAssignedAgent().getAgent_id().longValue());
    }




}
