package com.pradeep.smart_parking_api.exception;

public class SlotNotAvailableException extends RuntimeException {

    public SlotNotAvailableException(String message) {
        super(message);
    }
}
