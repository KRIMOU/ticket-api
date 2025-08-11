package com.pluralsight.ticket_api.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Status status;

    private LocalDateTime createdDateTime;

    private LocalDateTime closedDateTime;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent assignedAgent;

    private String resolutionSummary;

}
