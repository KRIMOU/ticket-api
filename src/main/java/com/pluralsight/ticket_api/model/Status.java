package com.pluralsight.ticket_api.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;

public enum Status {
    NEW,
    IN_PROGRESS,
    ASSIGNED,
    RESOLVED, CLOSED;
}
