package com.santdev.test.common.exception;

public class InvalidEncryptedIdException extends RuntimeException {

    public InvalidEncryptedIdException() {
        super("Invalid encrypted id");
    }
}
