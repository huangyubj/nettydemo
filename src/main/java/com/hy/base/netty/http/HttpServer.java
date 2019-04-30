package com.hy.base.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * Created with IntelliJ IDEA.
 * User: HYu
 * Date: 2019/4/29
 * Time: 14:25
 * Project: nettydemo
 */
public class HttpServer {
    private boolean isSSL;
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    private EventLoopGroup eventExecutors = new NioEventLoopGroup();
    public HttpServer(boolean isSSL) {
        this.isSSL = isSSL;
    }

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }

    public static void main(String[] args) throws CertificateException, SSLException, InterruptedException {
        HttpServer httpServer = new HttpServer(false);
        httpServer.init();
    }

    private void init() throws CertificateException, SSLException, InterruptedException {
        try {

            final SslContext sslContext;
            if(this.isSSL){
                //netty为我们提供的ssl加密，缺省
                SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();
            }else{
                sslContext = null;
            }
            serverBootstrap.group(eventExecutors)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(10102)
                    .childHandler(new ServerHandlerInitial(sslContext));
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }
}
