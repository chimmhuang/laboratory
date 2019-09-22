package com.chimm.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 实现客户端发送一个请求，服务器会返回hello netty
 *
 * @author chimm
 * @date 2019/9/22 0022
 */
public class HelloServer {

    public static void main(String[] args) throws Exception {

        //定义一对线程组
        //主线程组，用于接收客户端的连接，但是不做任何处理，跟老板一样，不做事
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //从线程组，老板线程组会把任务丢给他，让手下线程组去做任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //netty启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //netty服务器的配置
            serverBootstrap.group(bossGroup, workerGroup)   //设置主从线程
                    .channel(NioServerSocketChannel.class)  //设置nio的双向通道
                    .childHandler(new HttpServerCodec());   //子处理器，用于处理 workerGroup

            //启动server，并且设置8088为启动的端口号，启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            //监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅的关闭服务器
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
