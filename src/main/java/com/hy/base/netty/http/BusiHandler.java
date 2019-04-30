package com.hy.base.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/29
 * Time: 14:27
 * Project: nettydemo
 */
public class BusiHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String result = "";
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        String uri = fullHttpRequest.uri();
        if(!"/test".equalsIgnoreCase(uri)){
            result = "非法请求" + uri;
            send(ctx, result, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        HttpMethod httpMethod = fullHttpRequest.method();
        //处理get请求
        String body = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        if(HttpMethod.GET.equals(httpMethod)){
            System.out.println(uri + "---请求的消息为：" + body);
            result = new Date().toString() +  "--get message " + body + " and reveive";
            send(ctx,result, HttpResponseStatus.OK);
        }else if(HttpMethod.POST.equals(httpMethod)){

        }

    }

    private void send(ChannelHandlerContext ctx, String result, HttpResponseStatus status) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, status, Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
        fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8;");
        ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("链接客户端地址" + ctx.channel().remoteAddress());
    }
}
