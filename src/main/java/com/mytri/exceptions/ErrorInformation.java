package com.mytri.exceptions;

import lombok.Getter;

public class ErrorInformation {

    @Getter private final String message;
    @Getter private final String exception;

    public ErrorInformation(String message, String exception) {
        this.message = message;
        this.exception = exception;
    }
}
