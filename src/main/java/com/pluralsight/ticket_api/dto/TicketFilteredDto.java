package com.pluralsight.ticket_api.dto;

import com.pluralsight.ticket_api.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public record TicketFilteredDto (
        List<Status> statusList,
        LocalDateTime startDataTime,
        LocalDateTime endDataTime,
        String assignedAgent
        ){

}
