package com.facuferro.meetup.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DataIntegrity extends RuntimeException {
    public DataIntegrity(String s, DataIntegrityViolationException exc) {
        super(s,exc);
    }
}
