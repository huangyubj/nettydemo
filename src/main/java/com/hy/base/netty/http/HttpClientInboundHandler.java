package com.hy.base.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/29
 * Time: 14:26
 * Project: nettydemo
 */
public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse fullHttpResponse = (FullHttpResponse) msg;
        System.out.println(fullHttpResponse.headers());
        ByteBuf byteBuf = fullHttpResponse.content();
        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
