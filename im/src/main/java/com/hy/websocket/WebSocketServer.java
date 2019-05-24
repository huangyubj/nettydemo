package com.hy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/24
 * Time: 13:12
 * Project: nettydemo
 *
 */
//添加一个 ws 服务节点，用于web端绑定该节点
@ServerEndpoint("/ws/asset")
@Component
public class WebSocketServer {

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    //链接数量
    private static AtomicInteger count = new AtomicInteger(0);

    private static CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        sessions.add(session);
        int c = count.addAndGet(1);
        log.info(session.getId() + "链接成功,当前连接数量为：" + c + ",sessions.size()="+sessions.size());
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
        int c = count.addAndGet(-1);
        log.info(session.getId() + "断开链接成功,当前连接数量为：" + c + ",sessions.size()="+sessions.size());
    }

    @OnMessage
    public void onMessage(String msg, Session session){
        sendMessage(session, "收到消息，消息内容："+msg);
        log.info(session.getId() + "--收到消息：" + msg);
    }

    public static void sendMessage(Session session, String s) {
        try {
            session.getBasicRemote().sendText(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : sessions) {
            if(session.isOpen()){
                sendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void sendMessage(String sessionId,String message)
            throws IOException {
        Session session = null;
        for (Session s : sessions) {
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session!=null){
            sendMessage(session, message);
        }
        else{
            log.warn("没有找到你指定ID的会话：{}",sessionId);
        }
    }
}
