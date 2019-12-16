package com.chimm.muxin.netty;

import com.chimm.muxin.enums.MsgActionEnum;
import com.chimm.muxin.service.UserService;
import com.chimm.muxin.utils.JsonUtils;
import com.chimm.muxin.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，是用于为websocket专门处理的文本的对象，frame是消息的载体
 *
 * @author chimm
 * @date 2019/10/5 0005
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用户记录和管理所有客户端的channel
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        Channel currentChannel = ctx.channel();

        //1. 获取客户端传输过来的消息
        String content = msg.text();
        log.info("接收到数据：{}", content);
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        Integer action = dataContent.getAction();

        //2. 判断消息类型，根据不同的类型来处理不同的业务
        if (action == MsgActionEnum.CONNECT.type) {
            //2.1 当websocket第一次open的时候，初始化channel，把用户的channel和userId关联起来
            String senderId = dataContent.getChatMsg().getSenderId();
            UserChannelRel.put(senderId, currentChannel);
        } else if (action == MsgActionEnum.CHAT.type) {
            //2.2 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
            ChatMsg chatMsg = dataContent.getChatMsg();

            // 保存消息到数据库，并且标记为[未签收]
            UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
            String msgId = userService.saveMsg(chatMsg);
            chatMsg.setMsgId(msgId);

            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);

            // 发送消息
            // 从全局用户Channel关系中获取接收方的channel
            Channel receiverChannel = UserChannelRel.get(chatMsg.getReceiverId());
            if (receiverChannel == null) {
                // todo channel为空，代表用户离线，推送消息（JPush，个推，小米推送等）
            } else {
                // 当receiveChannel不为空的时候，从ChannelGroup去查找对应的channel是否存在
                Channel findChannel = users.find(receiverChannel.id());
                if (findChannel != null) {
                    // 用户在线
                    receiverChannel.writeAndFlush(
                            new TextWebSocketFrame(JsonUtils.objectToJson(dataContentMsg)));
                } else {
                    // 用户离线 todo 推送消息
                }
            }

        } else if (action == MsgActionEnum.SIGNED.type) {
            //2.3 签收消息类型，针对具体消息进行签收，修改数据库中对应消息的签收[已签收]
            UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
            // 扩展字段在signed类型的消息中，代表需要去签收的消息id，逗号间隔
            String msgIdsStr = dataContent.getExtand();
            String[] msgIds = msgIdsStr.split(",");

            List<String> msgIdList = new ArrayList<>();
            for (String msgId : msgIds) {
                if (StringUtils.isNotBlank(msgId)) {
                    msgIdList.add(msgId);
                }
            }

            if (!msgIdList.isEmpty()) {
                // 批量签收
                userService.updateMsgSigned(msgIdList);
            }


        } else if (action == MsgActionEnum.KEEPALIVE.type) {
            //2.4 心跳类型的消息
            log.info("收到来自channel为 [" + currentChannel + "] 的心跳包...");
        }

//        for (Channel channel : users) {
//            channel.writeAndFlush(new TextWebSocketFrame(
//                    "[服务器]在" + LocalDateTime.now() + "接收到消息，消息为：" + content));
//        }
//        //下面的这个方法和上面的for循环一致
////        clients.writeAndFlush(new TextWebSocketFrame(
////                "[服务器]在" + LocalDateTime.now() + "接收到消息，消息为：" + content));
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    /**
     * 助手类已移除
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应的客户端的channel
        // clients.remove(ctx.channel());
        log.info("客户端断开，channel对应的长id为：{}", ctx.channel().id().asLongText());
        log.info("客户端断开，channel对应的短id为：{}", ctx.channel().id().asShortText());
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("连接发生异常：" + cause.getMessage(), cause);
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx);
    }
}
