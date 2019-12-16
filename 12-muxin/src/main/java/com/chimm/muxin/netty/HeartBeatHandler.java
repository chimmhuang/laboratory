package com.chimm.muxin.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于检测channel的心跳handler
 * 继承 ChannelInboundHandlerAdapter，从而不需要实现 channelRead0()
 *
 * @author chimm
 * @date 2019/12/12
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 用户事件触发器
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        /**
         * IdleStateEvent
         * A user event triggered by {@link IdleStateHandler} when a {@link Channel} is idle.
         * 当 channel 连接空闲的时候，会触发 IdleStateEvent 事件
         */
        if (evt instanceof IdleStateEvent) {
            //判断 evt 是否是 IdleStateEvent（用户出发用户事件，包含 读空闲/写空闲/读写空闲）
            IdleStateEvent event = (IdleStateEvent) evt;

            /*
                我们只处理读写空闲的状态
             */
            if (event.state() == IdleState.READER_IDLE) {
                log.info("进入读空闲...");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("进入写空闲...");
            } else {
                //已经开启了飞行模式或者其他状态
                Channel channel = ctx.channel();
                // 关闭无用的channel，已防资源浪费
                channel.close();
            }
        }
    }
}
