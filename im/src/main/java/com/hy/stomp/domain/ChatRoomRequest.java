package com.hy.stomp.domain;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/23
 * Time: 14:44
 * Project: nettydemo
 */
public class ChatRoomRequest {
    private String name;
    private String chatValue;
    private String userId;



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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ChatRoomRequest{" +
                "name='" + name + '\'' +
                ", chatValue='" + chatValue + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
