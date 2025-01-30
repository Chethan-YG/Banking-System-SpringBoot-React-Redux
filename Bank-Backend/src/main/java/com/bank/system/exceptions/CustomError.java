package com.bank.system.exceptions;


public class CustomError extends RuntimeException {

    private static final long serialVersionUID = 1L;
	private int statusCode;

    public CustomError(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
