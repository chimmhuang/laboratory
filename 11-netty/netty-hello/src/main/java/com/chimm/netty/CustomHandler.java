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

    /**
     * channel注册
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。注册");
        super.channelRegistered(ctx);
    }

    /**
     * channel移除（取消注册）
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。移除");
        super.channelUnregistered(ctx);
    }

    /**
     * channel活跃
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。活跃");
        super.channelActive(ctx);
    }

    /**
     * channel不活跃
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。不活跃");
        super.channelInactive(ctx);
    }

    /**
     * channel读取
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel。。。读取");
        super.channelRead(ctx, msg);
    }

    /**
     * channel读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。读取完毕");
        super.channelReadComplete(ctx);
    }

    /**
     * 用户事件触发
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("用户事件。。。触发");
        super.userEventTriggered(ctx, evt);
    }

    /**
     * channel可写更改
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。可写更改");
        super.channelWritabilityChanged(ctx);
    }

    /**
     * 捕获到异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("捕获到异常");
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 助手类已添加
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类已添加");
        super.handlerAdded(ctx);
    }

    /**
     * 助手类已移除
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类已移除");
        super.handlerRemoved(ctx);
    }
}
