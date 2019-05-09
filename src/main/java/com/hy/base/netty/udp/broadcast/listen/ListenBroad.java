package com.hy.base.netty.udp.broadcast.listen;

import com.hy.base.netty.udp.broadcast.broad.NoticeBroad;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 14:07
 * Project: nettydemo
 * 消息监听者
 */
public class ListenBroad {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    //套接字选项
                    .option(ChannelOption.SO_BROADCAST, true)
                    //端口可重复
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .localAddress(NoticeBroad.PORT)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            //解码BroadMessage对象
                            ch.pipeline().addLast(new MessageDecoder());
                            //处理消息
                            ch.pipeline().addLast(new ListenBroadHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
