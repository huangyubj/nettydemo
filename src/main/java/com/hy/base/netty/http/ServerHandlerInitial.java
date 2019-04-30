package com.hy.base.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/29
 * Time: 14:22
 * Project: nettydemo
 */
public class ServerHandlerInitial extends ChannelInitializer {

    private final SslContext sslContext;

    public ServerHandlerInitial(SslContext sslContext){
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if(sslContext != null){
            //添加SSL处理器
            ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
        }
        ch.pipeline().addLast()
//                对response响应消息进行编码
                .addLast("encoder", new HttpResponseEncoder())
//                对request 请求体进行解码
                .addLast("decoder", new HttpRequestDecoder())
//               聚合http请求
                .addLast("aggre", new HttpObjectAggregator(1024*1024))
//                添加请求内容压缩
                .addLast("compressor", new HttpContentCompressor())
//                处理http业务
                .addLast("busi", new BusiHandler());
    }
}
