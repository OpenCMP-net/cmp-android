package com.opencmp.inapplib.errors;

public class ShowUiException extends Exception {

    public ShowUiException(String message) {
        super(message);
    }

    public ShowUiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShowUiException(Throwable cause) {
        super(cause);
    }
}
