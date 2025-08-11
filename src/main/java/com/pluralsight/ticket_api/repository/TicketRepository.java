package com.pluralsight.ticket_api.repository;

import com.pluralsight.ticket_api.model.Status;
import com.pluralsight.ticket_api.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("""
    SELECT t FROM Ticket t
    WHERE (:status IS NULL OR t.status IN :status)
    AND (:startDate IS NULL OR t.createdDateTime >= :startDate)
    AND (:endDate IS NULL OR t.createdDateTime <= :endDate)
    AND (:assignedAgent IS NULL OR t.assignedAgent.agent001 = :assignedAgent)
    """)
    List<Ticket> findWithFilters(List<Status> status, LocalDateTime startDate, LocalDateTime endDate, String assignedAgent);
}
