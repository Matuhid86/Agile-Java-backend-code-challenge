package com.agiletv.users.application.exception;

public class UserGeneratorServiceException extends
        Exception {
    public UserGeneratorServiceException(Throwable cause) {
        super(cause);
    }
    public UserGeneratorServiceException(String message) {
        super(message);
    }
}
