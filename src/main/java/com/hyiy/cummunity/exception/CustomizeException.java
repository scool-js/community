package com.hyiy.cummunity.exception;

public class CustomizeException extends RuntimeException{
    private String message;
    public CustomizeException(String message){
        this.message = message;
    }
    public CustomizeException(CustomizeErrorCode code){
        this.message = code.getMessage();
    }
    @Override
    public String getMessage() {
        return message;
    }
}
