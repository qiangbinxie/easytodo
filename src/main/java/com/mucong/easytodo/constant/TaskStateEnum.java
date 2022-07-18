package com.mucong.easytodo.constant;

public enum TaskStateEnum {
    TODO(0,"进行中"),
    COMPLETE(1,"完成"),
    EXPIRED(2,"过期");

    int state;
    String text;

    TaskStateEnum(int state,String text) {
        this.state = state;
        this.text = text;
    }

    public int getState() {
        return state;
    }

    public String getText() {
        return text;
    }
}
