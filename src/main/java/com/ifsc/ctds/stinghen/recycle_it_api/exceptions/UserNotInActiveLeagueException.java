package com.ifsc.ctds.stinghen.recycle_it_api.exceptions;

public class UserNotInActiveLeagueException extends RuntimeException {
    public UserNotInActiveLeagueException(String message) {
        super(message);
    }

    public UserNotInActiveLeagueException() {
    }

    public UserNotInActiveLeagueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotInActiveLeagueException(Throwable cause) {
        super(cause);
    }

    public UserNotInActiveLeagueException(String message, Throwable cause) {
        super(message, cause);
    }
}
