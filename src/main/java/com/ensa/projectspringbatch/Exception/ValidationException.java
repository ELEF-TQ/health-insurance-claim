package com.ensa.projectspringbatch.Exception;

public class ValidationException extends RuntimeException {

    // Constructeur avec un message d'erreur
    public ValidationException(String message) {
        super(message);
    }

}

