package com.hy.base.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/29
 * Time: 14:26
 * Project: nettydemo
 */
public class HttpClient {
    public static void main(String[] args) throws InterruptedException, URISyntaxException, UnsupportedEncodingException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //为什么链式编程发不出去？？？pipeline对象也一样
//                            ChannelPipeline channelPipeline = ch.pipeline().addLast(new HttpClientCodec());
//                            System.out.println(channelPipeline);
//                            ChannelPipeline channelPipeline1 = channelPipeline.addLast("aggre", new HttpObjectAggregator(10*1024*1024));
//                            System.out.println(channelPipeline.equals(channelPipeline1));
//                                    channelPipeline1.addLast("compress", new HttpContentCompressor())
//                                    .addLast(new HttpClientInboundHandler());
//                            System.out.println(ch.pipeline().equals(ch.pipeline()));
//                            System.out.println(ch.pipeline().equals(channelPipeline));
//                            System.out.println(ch.pipeline());
//                            System.out.println(ch.pipeline());
//                            System.out.println(ch.pipeline());
//                            System.out.println(ch.pipeline());
                            ch.pipeline().addLast(new HttpClientCodec());
                            ch.pipeline().addLast("aggre", new HttpObjectAggregator(10*1024*1024));
                            ch.pipeline().addLast("compress",new HttpContentDecompressor());
                            ch.pipeline().addLast(new HttpClientInboundHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 10102).sync();
            URI uri = new URI("/test");
            FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uri.toASCIIString(), Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));
            //为什么链式编程发不出去？？？
            fullHttpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            fullHttpRequest.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
            fullHttpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, fullHttpRequest.content().readableBytes());
            channelFuture.channel().write(fullHttpRequest);
            channelFuture.channel().flush();
            channelFuture.channel().closeFuture();
        }finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }
}
