package com.hy.base.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/25
 * Time: 15:45
 * Project: nettydemo
 * 检测空闲链接及超时处理
 * 当超过60秒没有数据收到时，就会得到通知，此时就发送心跳到远端，如果没有回应，连接就关闭
 */
public class IdleStateHandlerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS))
                .addLast(new HeatChannelHandler());
    }

    /**
     * 心跳检测ChannelHandler
     */
    public static class HeatChannelHandler extends ChannelInboundHandlerAdapter {
        public static final ByteBuf HEART_MESSAGE = Unpooled.copiedBuffer("HEART_INFO", CharsetUtil.UTF_8);
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleStateEvent){
                ctx.writeAndFlush(HEART_MESSAGE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }else{
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
