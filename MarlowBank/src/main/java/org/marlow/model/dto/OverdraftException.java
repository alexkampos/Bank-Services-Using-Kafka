package org.marlow.model.dto;

public class OverdraftException extends RuntimeException {

    public OverdraftException(String msg) {
        super(msg);
    }

}
