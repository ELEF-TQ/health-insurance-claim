package com.ensa.projectspringbatch.Exception;

public class NonRecoverableException extends RuntimeException {
    public NonRecoverableException(String message) {
        super(message);
    }
}