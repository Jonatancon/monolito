package com.pragma.monolito.exception;

public class CoreException extends RuntimeException {

    private final String userMessage;
    private final int status;
    private final String codeError;

    public CoreException(String developerMessage, String userMessage, int status, String codeError) {
        super(developerMessage);
        this.userMessage = userMessage;
        this.status = status;
        this.codeError = codeError;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getCodeError() {
        return this.codeError;
    }
}
