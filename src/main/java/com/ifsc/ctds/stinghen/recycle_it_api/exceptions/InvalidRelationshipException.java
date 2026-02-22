package com.ifsc.ctds.stinghen.recycle_it_api.exceptions;

public class InvalidRelationshipException extends RuntimeException {
    public InvalidRelationshipException(String message) {
        super(message);
    }

    public InvalidRelationshipException() {
    }

    public InvalidRelationshipException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidRelationshipException(Throwable cause) {
        super(cause);
    }

    public InvalidRelationshipException(String message, Throwable cause) {
        super(message, cause);
    }
}
