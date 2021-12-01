package com.ssportup.exception;

/**
 * Класс пользовательского исключения.
 *
 * @author habatoo
 */
public class BusinessLogicException extends RuntimeException {

    private final String code;
    private Throwable cause;

    public BusinessLogicException(String message,  String code) {
        super(message);
        this.code = code;
    }

    public BusinessLogicException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("BusinessLogicException{message=%s code=%s}", getMessage(), getCode());
    }
}
