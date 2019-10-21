package com.chimm.muxin.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 实现客户端发送一个请求，服务器会返回hello netty
 *
 * @author chimm
 * @date 2019/9/22 0022
 */
@Slf4j
@Component
public class WSServer {

    /** 单例对象 */
    public static class SingletonWSServer {
        private SingletonWSServer() { }

        static final WSServer instance = new WSServer();
    }

    /** 获取单例对象 */
    public static WSServer getInstance() {
        return SingletonWSServer.instance;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    //@Value("${server.port}")
    private Integer nettyPort = 8000;

    public WSServer() {
        //定义一对线程组
        //主线程组，用于接收客户端的连接，但是不做任何处理，跟老板一样，不做事
        mainGroup = new NioEventLoopGroup();

        //从线程组，老板线程组会把任务丢给他，让手下线程组去做任务
        subGroup = new NioEventLoopGroup();

        //netty启动类
        serverBootstrap = new ServerBootstrap();
        //netty服务器的配置
        serverBootstrap.group(mainGroup, subGroup)   //设置主从线程
                .channel(NioServerSocketChannel.class)  //设置nio的双向通道
                .childHandler(new WSServerInitializer());   //子处理器，用于处理 workerGroup
    }

    /**
     * 启动服务器
     */
    public void start() {
        this.channelFuture = serverBootstrap.bind(nettyPort);
        log.info("#####################################");
        log.info("# netty webSocket server 启动完毕");
        log.info("#####################################");
    }
}
