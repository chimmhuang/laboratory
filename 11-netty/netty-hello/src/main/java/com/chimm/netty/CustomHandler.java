package com.chimm.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 创建自定义助手类
 *
 * @author huangshuai
 * @date 2019-09-24
 */
// SimpleChannelInboundHandler：对于请求来讲，其实相当于[入站、入境]
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

    //从缓存区中取出数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        if (httpObject instanceof HttpRequest) {
            //获取当前的channel
            Channel channel = channelHandlerContext.channel();

            //显示客户端的远程地址
            System.out.println(channel.remoteAddress());

            //定义发送的消息
            //将数据拷贝到缓冲区
            ByteBuf content = Unpooled.copiedBuffer("hello netty~", CharsetUtil.UTF_8);

            //构建一个http response
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            content);
            //为响应增加数据类型和长度
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将数据写到缓存区，并刷新到客户端
            channelHandlerContext.writeAndFlush(response);
        }

    }
}
