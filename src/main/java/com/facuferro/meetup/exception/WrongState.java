package com.facuferro.meetup.exception;

public class WrongState
     extends RuntimeException {

    public WrongState(String description) {
            super(description);
        }
}
