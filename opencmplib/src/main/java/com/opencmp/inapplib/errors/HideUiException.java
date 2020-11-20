package com.opencmp.inapplib.errors;

public class HideUiException extends Exception {

    public HideUiException(String message) {
        super(message);
    }

    public HideUiException(String message, Throwable cause) {
        super(message, cause);
    }

    public HideUiException(Throwable cause) {
        super(cause);
    }
}
