package com.philemonworks.critter;

public class SqlRuntimeException extends RuntimeException {
    public SqlRuntimeException(final Throwable cause) {
        super(cause);
    }

    public SqlRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
