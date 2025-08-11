package com.pluralsight.ticket_api.repository;

import com.pluralsight.ticket_api.model.Status;
import com.pluralsight.ticket_api.model.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TicketRepositoryTest {
    @Autowired
    public TicketRepository ticketRepository;


    @Test
    @Sql({"/filterTestData.sql"})
    void givenStatus_whenGettingTickets_thenTicketsWithMatchingStatusAreReturned(){
        List<Ticket> ticketList = ticketRepository.findWithFilters(List.of(Status.IN_PROGRESS),null,null,null);
        assertEquals(1 , ticketList.size());
    }
}
