package com.hy.base.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/22
 * Time: 13:59
 * Project: nettydemo
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 1.@Sharable 标识这类的实例之间可以在 channel 里面共享
     *
     * 2.日志消息输出到控制台
     *
     * 3.将所接收的消息返回给发送者。注意，这还没有冲刷数据
     *
     * 4.冲刷所有待审消息到远程节点。关闭通道后，操作完成
     *
     * 5.打印异常堆栈跟踪
     *
     * 6.关闭通道
     */

    /**
     * 每个信息入站都会调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(
                "Client " + ctx.channel().remoteAddress() + " connected");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));        //2
        ctx.write(in);                            //3
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//4
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();                //5
        ctx.close();                            //6
    }
}
