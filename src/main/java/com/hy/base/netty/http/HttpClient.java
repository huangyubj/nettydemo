package com.hy.base.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

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
    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpClientCodec())
                                    .addLast("aggre", new HttpObjectAggregator(1024*1024))
                                    .addLast("compress", new HttpContentCompressor())
                                    .addLast(new HttpClientInboundHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(10102)).sync();
            URI uri = new URI("/test");
            FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uri.toASCIIString(), Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));
            fullHttpRequest.headers().set(HttpHeaderNames.KEEP_ALIVE, HttpHeaderValues.KEEP_ALIVE)
                    .set(HttpHeaderNames.HOST, "127.0.0.1")
                    .set(HttpHeaderNames.CONTENT_LENGTH, fullHttpRequest.content().readableBytes());
            channelFuture.channel().writeAndFlush(fullHttpRequest);
            channelFuture.channel().closeFuture();
        }finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }
    }
}
