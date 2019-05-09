package com.hy.base.netty.udp.broadcast.listen;

import com.hy.base.netty.udp.broadcast.BroadMessage;
import com.hy.base.netty.udp.broadcast.broad.NoticeBroad;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 14:12
 * Project: nettydemo
 */
public class MessageDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("decode--connet");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        //解析并构建一个新的BroadMessage对象
        ByteBuf byteBuf = msg.content();
        long time = byteBuf.readLong();
        int idx = byteBuf.readerIndex();
        String message = byteBuf.slice(idx, byteBuf.readableBytes()).toString(CharsetUtil.UTF_8);
        String[] ms = message.split(NoticeBroad.SEPARATOR);
        BroadMessage broadMessage = new BroadMessage(time, ms[0], ms[1], (InetSocketAddress) ctx.channel().remoteAddress());
        //作为本handler的处理结果，交给后面的handler进行处理
        out.add(broadMessage);
    }
}
