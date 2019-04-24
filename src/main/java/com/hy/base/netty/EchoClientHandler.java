package com.hy.base.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/22
 * Time: 14:55
 * Project: nettydemo
 */
public class EchoClientHandler extends SimpleChannelInboundHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //写了100次，实际只接收了2次
        //1.换行符来区分
//        String lineSeparator = System.getProperty("line.separator");
        //2.自定义 分隔符
//        String lineSeparator = EchoServer.DELIMITER_STR;
        //3.消息定长发送
        ByteBuf byteBuf;
        String lineSeparator = "";
        for (int i = 0; i < 100; i++) {
            //3消息定长发送
            byteBuf = Unpooled.buffer(EchoServer.FIXED_MESSAGE.length());
            byteBuf.writeBytes(EchoServer.FIXED_MESSAGE.getBytes());
            ctx.writeAndFlush(byteBuf);
            //1、2
//            String msg = "粘包半包,拆包!" + lineSeparator;
//            ctx.writeAndFlush(Unpooled.copiedBuffer(msg, //2
//                    CharsetUtil.UTF_8));
        }
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client received: " + ((ByteBuf)msg).toString(CharsetUtil.UTF_8));
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {                    //4
        cause.printStackTrace();
        ctx.close();
    }
}
