package org.marlow.model.dto;

public class AmountNotPositiveNumber  extends RuntimeException {

    public AmountNotPositiveNumber(String msg) {
        super(msg);
    }

}
