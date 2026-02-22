package com.ifsc.ctds.stinghen.recycle_it_api.exceptions;

public class BadValueException extends RuntimeException {
    public BadValueException(String message) {
        super(message);
    }

    public BadValueException() {
    }

    public BadValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BadValueException(Throwable cause) {
        super(cause);
    }

    public BadValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
