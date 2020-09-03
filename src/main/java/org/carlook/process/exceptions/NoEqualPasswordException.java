package org.carlook.process.exceptions;

public class NoEqualPasswordException extends Exception{
    private String reason;


    public NoEqualPasswordException( String reason ) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
