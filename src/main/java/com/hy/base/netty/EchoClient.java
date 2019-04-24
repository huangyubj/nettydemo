package com.hy.base.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/22
 * Time: 14:59
 * Project: nettydemo
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("127.0.0.1", 10101).start();
    }
    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();/*线程组*/
        try {
            Bootstrap b = new Bootstrap();                //1/*客户端启动必备*/  Bootstrap
            b.group(group)                                //2
                    .channel(NioSocketChannel.class)            //3 /*指明使用NIO进行网络通讯*/  NioSocketChannel
                    .remoteAddress(new InetSocketAddress(host, port))    //4 /*配置远程服务器的地址*/
                    .handler(new ChannelInitializer<SocketChannel>() {    //5
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {

                            //1.换行符来区分
//                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024*1000));
                            //2.自定义 分隔符
//                            ByteBuf byteBuf = Unpooled.copiedBuffer(EchoServer.DELIMITER_STR.getBytes());
//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*1000, byteBuf));
                            //3.消息定长发送
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(EchoServer.FIXED_MESSAGE.length()));
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();        //6

            f.channel().closeFuture().sync();            //7

        } finally {
            group.shutdownGracefully().sync();            //8
        }
    }

    public void starttoo() throws InterruptedException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
        }finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }
}
