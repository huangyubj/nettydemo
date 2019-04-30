package com.hy.base.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/25
 * Time: 15:07
 * Project: nettydemo
 */
public class FrameChunkDecode extends ByteToMessageDecoder {
    private static final int MAX_SIZE = 1024;
    //在解码时处理太大的帧
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readSize = in.readableBytes();
        if( readSize > MAX_SIZE){
            in.skipBytes(readSize);//忽略所有可读的字节
            throw new TooLongFrameException();
        }
    }
}
