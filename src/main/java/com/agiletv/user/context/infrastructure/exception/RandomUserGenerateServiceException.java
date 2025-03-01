package com.agiletv.user.context.infrastructure.exception;

public class RandomUserGenerateServiceException extends
        Exception {
    public RandomUserGenerateServiceException(Throwable cause) {
        super(cause);
    }
    public RandomUserGenerateServiceException(String message) {
        super(message);
    }
}
