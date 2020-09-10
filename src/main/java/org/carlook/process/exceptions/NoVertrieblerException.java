package org.carlook.process.exceptions;

public class NoVertrieblerException extends Exception {
    private String reason;

    public NoVertrieblerException(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

