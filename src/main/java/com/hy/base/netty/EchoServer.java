package com.hy.base.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/22
 * Time: 14:34
 * Project: nettydemo
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(10101).start();
    }

    /**
     * 创建 ServerBootstrap 实例来引导服务器并随后绑定
     * 创建并分配一个 NioEventLoopGroup 实例来处理事件的处理，如接受新的连接和读/写数据。
     * 指定本地 InetSocketAddress 给服务器绑定
     * 通过 EchoServerHandler 实例给每一个新的 Channel 初始化
     * 最后调用 ServerBootstrap.bind() 绑定服务器
     */
    public void start() throws Exception {
        //3.创建 EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup(); //3
        try {
//            4.创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)                                //4
//            5.指定使用 NIO 的传输 Channel
                    .channel(NioServerSocketChannel.class)        //5
//            6.设置 socket 地址使用所选的端口
                    .localAddress(new InetSocketAddress(port))    //6
//            7.添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoServerHandler());
                        }
                    });
//            8.绑定的服务器;sync 等待服务器关闭
            ChannelFuture f = b.bind().sync();            //8
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
//            9.关闭 channel 和 块，直到它被关闭
            f.channel().closeFuture().sync();            //9
        } finally {
//            10.关机的 EventLoopGroup，释放所有资源。
            group.shutdownGracefully().sync();            //10
        }
    }
}
