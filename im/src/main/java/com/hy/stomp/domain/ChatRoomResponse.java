package com.hy.stomp.domain;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/23
 * Time: 14:45
 * Project: nettydemo
 */
public class ChatRoomResponse {

    private String name;
    private String chatValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatValue() {
        return chatValue;
    }

    public void setChatValue(String chatValue) {
        this.chatValue = chatValue;
    }
}
