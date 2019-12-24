package com.chimm.example05;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author huangshuai
 * @date 2019/12/23
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());

        // 按块写
        pipeline.addLast(new ChunkedWriteHandler());
        /*
            http消息聚合器
            若不添加该 handler，可能会出现的状态：一次请求，netty服务接受到了两次，一次是 http header 信息，一次是具体的内容
            该聚合器就是将这些信息聚合为一个
         */
        pipeline.addLast(new HttpObjectAggregator(8192));

        /*
            完成 webSocket 的一些繁重的处理
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}
