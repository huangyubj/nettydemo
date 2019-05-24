package com.hy.stomp.controller;

import com.hy.stomp.domain.ChatRoomRequest;
import com.hy.stomp.domain.ChatRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/21
 * Time: 16:38
 * Project: nettydemo
 */
@Controller
public class StompController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/massRequest")
    @SendTo("/mass/getResponse")//发送地址
    public ChatRoomResponse mass(ChatRoomRequest request){
        System.out.println(request.toString());
        //收到消息，转换成response发送到群聊去
        ChatRoomResponse chatRoomResponse = new ChatRoomResponse();
        chatRoomResponse.setChatValue(request.getChatValue());
        chatRoomResponse.setName(request.getName());
        return chatRoomResponse;
    }

    @MessageMapping("/aloneRequest")
    public ChatRoomResponse queue(ChatRoomRequest request){
        System.out.println(request.toString());
        //收到消息，转换成response发送到群聊去
        ChatRoomResponse chatRoomResponse = new ChatRoomResponse();
        chatRoomResponse.setChatValue(request.getChatValue());
        chatRoomResponse.setName(request.getName());
        template.convertAndSendToUser(request.getUserId()+"", "/along", chatRoomResponse);
        return chatRoomResponse;
    }
}
