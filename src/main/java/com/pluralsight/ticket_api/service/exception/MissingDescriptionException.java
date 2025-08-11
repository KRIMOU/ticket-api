package com.pluralsight.ticket_api.service.exception;

public class MissingDescriptionException extends RuntimeException {
    public MissingDescriptionException(String s) {
        super(s);
    }
}
