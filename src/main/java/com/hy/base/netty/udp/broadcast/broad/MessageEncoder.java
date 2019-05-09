package com.hy.base.netty.udp.broadcast.broad;

import com.hy.base.netty.udp.broadcast.BroadMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/5/9
 * Time: 11:31
 * Project: nettydemo
 */
public class MessageEncoder extends MessageToMessageEncoder<BroadMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, BroadMessage msg, List<Object> out) throws Exception {
        long time = msg.getTime();
        byte[] titles = msg.getTitle().getBytes();
        byte[] contents = msg.getMessage().getBytes();
        byte[] separs = NoticeBroad.SEPARATOR.getBytes();
        //自定义序列化BroadMessage 对象------ByteBuf默认分配的内存大小为256，超出256消息会发送不出去，但是也不抛错出来，是坑，是坑，是坑
        ByteBuf byteBuf = ctx.alloc().buffer(1024*8);
        byteBuf.writeLong(time);
        byteBuf.writeBytes(titles);
        byteBuf.writeBytes(separs);
        byteBuf.writeBytes(contents);
        //将一个拥有数据和目的地地址的新 DatagramPacket 添加到出站的消息列表中
        out.add(new DatagramPacket(byteBuf, msg.getAddress()));
        System.out.println("broadcast："+msg.toString());
    }
}
