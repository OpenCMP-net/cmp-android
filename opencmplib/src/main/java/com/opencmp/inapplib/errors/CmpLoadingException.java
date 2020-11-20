package com.opencmp.inapplib.errors;

public class CmpLoadingException extends Exception {

    public CmpLoadingException(String message) {
        super(message);
    }

    public CmpLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CmpLoadingException(Throwable cause) {
        super(cause);
    }
}
