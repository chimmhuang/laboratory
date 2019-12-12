package com.chimm.muxin.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chimm
 * @date 2019/12/12
 */
@Slf4j
/**
 * 若继承 SimpleChannelInboundHandler，则必须实现channelRead0()，如果不想实现，则继承它的父类
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 用户事件触发器
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
