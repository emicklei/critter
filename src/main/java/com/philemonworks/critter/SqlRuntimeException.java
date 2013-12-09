package com.philemonworks.critter;

public class SqlRuntimeException extends RuntimeException {
    public SqlRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
