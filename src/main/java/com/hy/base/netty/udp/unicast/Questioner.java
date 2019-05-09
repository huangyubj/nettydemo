package com.hy.base.netty.udp.unicast;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 10:19
 * Project: nettydemo
 * UDP 单向传输，提问者  <-----> 回答者
 * 和socket并没什么太大区别，
 * 1.udp不用连接
 * 2.没有客户端、服务端的明确区别
 * 3.发送的数据包为DatagramPackt
 */
public class Questioner {
    public static final String QUESTION = "来一句唐诗";

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(new QuestionerHandler());
            ChannelFuture channelFuture = bootstrap.bind(10101).sync();
            System.out.println("put a question:"+QUESTION);
            channelFuture.channel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(QUESTION, CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", 10102)));
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
