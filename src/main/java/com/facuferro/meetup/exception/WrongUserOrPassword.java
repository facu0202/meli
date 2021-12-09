package com.facuferro.meetup.exception;

public class WrongUserOrPassword
     extends RuntimeException {

    public WrongUserOrPassword(String description) {
            super(description);
        }
}
