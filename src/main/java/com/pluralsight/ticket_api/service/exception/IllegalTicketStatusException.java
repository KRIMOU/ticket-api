package com.pluralsight.ticket_api.service.exception;

public class IllegalTicketStatusException extends RuntimeException {

    public IllegalTicketStatusException(String message){
        super(message);
    }

}
