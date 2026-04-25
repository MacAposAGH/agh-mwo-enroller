package com.company.enroller.persistence;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("errorHandler")
public class ErrorHandler {

    private ResponseEntity<?> throwConflictResponse(String message) {
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    public ResponseEntity<?> entityAlreadyExist() {
        return throwConflictResponse("Entity already exists");
    }

    public ResponseEntity<?> entityDoesntExist() {
        return throwConflictResponse("Entity doesn't exist");
    }

}
