package com.hy.base.netty.udp.broadcast.listen;

import com.hy.base.netty.udp.broadcast.BroadMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 14:09
 * Project: nettydemo
 */
public class ListenBroadHandler extends SimpleChannelInboundHandler<BroadMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BroadMessage msg) throws Exception {
        System.out.println(msg.toString());
    }
}
