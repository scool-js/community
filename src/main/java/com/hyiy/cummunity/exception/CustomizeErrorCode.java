package com.hyiy.cummunity.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOTE_FOUND(2001,"你找的问题不在了，要不要换个试试"),
    TARGET_PARAM_NOTE_FOUND(2002,"未选中任何问题或者回复"),
    NO_LOGIN(2003,"没登陆不能进行评论，请先登录"),
    SYSTEM_ERROR(2004,"服务器冒烟啦"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你回复的评论已不存在"),
    CONTENT_IS_EMPTY(2007,"输入的内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"要读取的信息不属于你"),
    NOTIFICATION_NOT_FOUND(2009,"通知未找到")

    ;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }
    private String message;
    private Integer code;
    CustomizeErrorCode(Integer code, String message){
        this.message = message;
        this.code = code;
    }

}
