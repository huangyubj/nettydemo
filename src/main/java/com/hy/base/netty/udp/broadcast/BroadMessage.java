package com.hy.base.netty.udp.broadcast;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 13:10
 * Project: nettydemo
 */
public class BroadMessage {
    private long time;
    private String message;
    private String title;

    private InetSocketAddress address;

    private  BroadMessage(){}

    public BroadMessage(long time, String message, String title, InetSocketAddress address) {
        this.time = time;
        this.message = message;
        this.title = title;
        this.address = address;
    }

    public BroadMessage(long time, String message, InetSocketAddress address) {
        this(time, message, "", address);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return  new Date(time) +
                "\n" + title + '\n' +
                 message;
    }
}
