package com.pluralsight.ticket_api.service.exception;

public class TicketDoneException extends RuntimeException {
    public TicketDoneException(String message) {
        super(message);
    }
}
