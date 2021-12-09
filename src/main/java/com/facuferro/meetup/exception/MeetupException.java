package com.facuferro.meetup.exception;

public class MeetupException
        extends RuntimeException {

    public MeetupException(String description) {
        super(description);
    }
}
