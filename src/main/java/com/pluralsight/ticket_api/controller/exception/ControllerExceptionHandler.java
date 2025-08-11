package com.pluralsight.ticket_api.controller.exception;

import com.pluralsight.ticket_api.service.exception.AgentNotFoundException;
import com.pluralsight.ticket_api.service.exception.IllegalTicketStatusException;
import com.pluralsight.ticket_api.service.exception.TicketDoneException;
import com.pluralsight.ticket_api.service.exception.TicketNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(IllegalTicketStatusException.class)
    public ResponseEntity<String> handleInvalidTicketState(IllegalTicketStatusException illegalTicketStatusException){
        return new ResponseEntity<>(illegalTicketStatusException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgentNotFoundException.class)
    public ResponseEntity<String> handleAgentNotFoundException(AgentNotFoundException agentNotFoundException){
        return new ResponseEntity<>(agentNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<String> handleAgentNotFoundException(TicketNotFoundException ticketNotFoundException){
        return new ResponseEntity<>(ticketNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketDoneException.class)
    public ResponseEntity<String> handleClosedTicketException(TicketDoneException ticketDoneException){
        return new ResponseEntity<>(ticketDoneException.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
