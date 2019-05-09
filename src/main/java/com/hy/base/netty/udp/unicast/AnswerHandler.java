package com.hy.base.netty.udp.unicast;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 10:43
 * Project: nettydemo
 */
public class AnswerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String message = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println("get a message:"+message);
        if(Questioner.QUESTION.equals(message)){
            ctx.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("熟读唐诗三百首，不会作诗也会吟", CharsetUtil.UTF_8),
                    msg.sender()));
        }
    }
}
