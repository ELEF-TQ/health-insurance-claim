package com.ensa.projectspringbatch.Exception;

public class RecoverableException extends RuntimeException {
    public RecoverableException(String message) {
        super(message);
    }
}
