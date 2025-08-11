package com.pluralsight.ticket_api.dto;

import com.pluralsight.ticket_api.model.Status;

import java.time.LocalDateTime;


public record TicketDto(Long id,
                        String description,
                        Status status,
                        LocalDateTime createdDateTime,
                        LocalDateTime closedDateTime,
                        com.pluralsight.ticket_api.model.Agent assignedAgent,
                        String resolutionSummary){
}
