package com.hy.base.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.concurrent.Future;

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
    public static final String DELIMITER_STR = "_HY_DELIMITER_";
    public static final String FIXED_MESSAGE = "this is a messagte";
    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(10101).startToo();
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
            ServerBootstrap b = new ServerBootstrap();  //ServerBootstrap
            b.group(group)                                //4
//            5.指定使用 NIO 的传输 Channel
                    .channel(NioServerSocketChannel.class)        //5   NioServerSocketChannel
//            6.设置 socket 地址使用所选的端口
                    .localAddress(new InetSocketAddress(port))    //6
//            7.添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoServerHandler());  //  Sharable的handler可以共享  即 只new 一个实例   EchoServerHandler esh = new EchoServerHandler();
                                                                //  addLast(esh);
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


    public void startToo() throws InterruptedException {
        //一个EventLoop可绑定多个Channle
        //ChannlePipeline 链接ChannelHanddler

//        1.bootstrp
//        2.group
//        3.指定channel
//        4.设置地址localAddress
//        5.服务添加childHanddler 处理器，客户端添加handdler,一个ChannelHandler 可以直接new，多个用ChannelInitializer，用ChannelPipeline添加多个
//        5.1为pipeline添加handler处理器
//        6.服务bind地址
//        7.关闭future
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(nioEventLoopGroup).channel(NioServerSocketChannel.class)
                    .localAddress(port).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //1.换行符来区分
//                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024*1000));
                    //2.自定义 分隔符
//                    ByteBuf byteBuf = Unpooled.copiedBuffer(EchoServer.DELIMITER_STR.getBytes());
//                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*1000, byteBuf));
                    //3.消息定长发送
                    ch.pipeline().addLast(new FixedLengthFrameDecoder(EchoServer.FIXED_MESSAGE.length()));
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            }).childOption(ChannelOption.TCP_NODELAY, true);
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }
    }
}
