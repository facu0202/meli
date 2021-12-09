package com.facuferro.meetup.exception;

public class NotFound
     extends RuntimeException {


    public NotFound(String description) {
            super(description);
        }
}
