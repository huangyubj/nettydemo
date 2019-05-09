package com.hy.base.netty.udp.broadcast.broad;

import com.hy.base.netty.udp.broadcast.BroadMessage;
import com.hy.base.netty.udp.broadcast.ConstantMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 11:21
 * Project: nettydemo
 * 消息广播者
 *
 */
public class NoticeBroad {
    public static final int PORT = 10103;
    public static final String SEPARATOR = "~!@#";
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new MessageEncoder());
                        }
                    });
            List<Map<String, String>> list = ConstantMessage.list;
            InetSocketAddress address = new InetSocketAddress("255.255.255.255", PORT);
            Channel channel = bootstrap.bind(0).sync().channel();
//            while(true){
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    BroadMessage broadMessage = new BroadMessage(Calendar.getInstance().getTimeInMillis(),
                            map.get(ConstantMessage.KEY_CONTENT), map.get(ConstantMessage.KEY_TITLE), address);
                    channel.writeAndFlush(broadMessage);
                    Thread.sleep(2000);
                }
//            }
            channel.closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
