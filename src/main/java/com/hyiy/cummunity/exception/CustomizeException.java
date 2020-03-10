package com.hyiy.cummunity.exception;

public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;
    public CustomizeException(CustomizeErrorCode code){
        this.code = code.getCode();
        this.message = code.getMessage();
    }
    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
