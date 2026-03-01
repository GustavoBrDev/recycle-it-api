package com.ifsc.ctds.stinghen.recycle_it_api.exceptions;

public class DeniedRequestException extends RuntimeException {
    public DeniedRequestException(String message) {
        super(message);
    }

    public DeniedRequestException() {
    }

    public DeniedRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DeniedRequestException(Throwable cause) {
        super(cause);
    }

    public DeniedRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
