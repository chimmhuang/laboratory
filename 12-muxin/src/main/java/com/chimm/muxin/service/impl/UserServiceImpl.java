package com.chimm.muxin.service.impl;

import com.chimm.muxin.domain.FriendsRequest;
import com.chimm.muxin.domain.MyFriends;
import com.chimm.muxin.domain.Users;
import com.chimm.muxin.domain.vo.FriendRequestVO;
import com.chimm.muxin.domain.vo.MyFriendsVO;
import com.chimm.muxin.enums.MsgSignFlagEnum;
import com.chimm.muxin.enums.SearchFriendsStatusEnum;
import com.chimm.muxin.mapper.ChatMsgMapper;
import com.chimm.muxin.mapper.FriendsRequestMapper;
import com.chimm.muxin.mapper.MyFriendsMapper;
import com.chimm.muxin.mapper.UsersMapper;
import com.chimm.muxin.netty.ChatMsg;
import com.chimm.muxin.service.UserService;
import com.chimm.muxin.utils.FastDFSClient;
import com.chimm.muxin.utils.FileUtils;
import com.chimm.muxin.utils.QRCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author huangshuai
 * @date 2019/11/5
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper userMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private MyFriendsMapper myFriendsMapper;
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;
    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        Users user = new Users();
        user.setUsername(username);

        Users result = userMapper.selectOne(user);

        return result != null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String pwd) {

        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", pwd);

        Users result = userMapper.selectOneByExample(userExample);

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users user) {
        String userId = sid.nextShort();

        // 为每个用户生成一个唯一的二维码
        String qrCodePath = "F://" + userId + "qrcode.png";
        // 生成规则：  muxin_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath, "muxin_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);

        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            log.error("二维码文件上传出错：{}", e.getMessage(), e);
        }
        user.setQrCode(qrCodeUrl);
        user.setId(userId);
        userMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        userMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    protected Users queryUserById(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {

        Users user = queryUserInfoByUsername(friendUsername);

        // 1. 搜索的用户如果不存在，返回[无此用户]
        if (user == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }

        // 2. 搜索账号是你自己，返回[不能添加自己]
        if (user.getId().equals(myUserId)) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }

        // 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Example example = Example.builder(MyFriends.class)
                .build();
        example.createCriteria()
                .andEqualTo("myUserId", myUserId)
                .andEqualTo("myFriendUserId",user.getId());

        MyFriends myFriends = myFriendsMapper.selectOneByExample(example);

        if (myFriends != null) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfoByUsername(String username) {
        return userMapper.selectOne(Users.builder()
                .username(username)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {

        // 根据用户名把朋友信息查询出来
        Users friend = queryUserInfoByUsername(friendUsername);

        // 1. 查询发送好友请求记录表
        Example example = Example.builder(FriendsRequest.class)
                .build();
        example.createCriteria()
                .andEqualTo("sendUserId", myUserId)
                .andEqualTo("acceptUserId", friend.getId());
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(example);

        if (friendsRequest == null) {
            // 2. 如果不是你的好友，并且好友记录没有添加，则新增好友请求记录
            String requestId = sid.nextShort();

            friendsRequestMapper.insert(FriendsRequest.builder()
                    .id(requestId)
                    .sendUserId(myUserId)
                    .acceptUserId(friend.getId())
                    .requestDateTime(new Date())
                    .build());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return userMapper.queryFriendRequestList(acceptUserId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {
        friendsRequestMapper.delete(FriendsRequest.builder()
                .sendUserId(sendUserId)
                .acceptUserId(acceptUserId)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        saveFriends(sendUserId, acceptUserId);
        saveFriends(acceptUserId, sendUserId);
        deleteFriendRequest(sendUserId, acceptUserId);
        deleteFriendRequest(acceptUserId, sendUserId);

//        Channel sendChannel = UserChannelRel.get(sendUserId);
//        if (sendChannel != null) {
//            // 使用websocket主动推送消息到请求发起者，更新他的通讯录列表为最新
//            DataContent dataContent = new DataContent();
//            dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);
//
//            sendChannel.writeAndFlush(
//                    new TextWebSocketFrame(
//                            JsonUtils.objectToJson(dataContent)));
//        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void saveFriends(String sendUserId, String acceptUserId) {
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyFriendUserId(acceptUserId);
        myFriends.setMyUserId(sendUserId);
        myFriendsMapper.insert(myFriends);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        return userMapper.queryMyFriends(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsg chatMsg) {

        com.chimm.muxin.domain.ChatMsg msgDB = new com.chimm.muxin.domain.ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMsg.getMsg());

        chatMsgMapper.insertSelective(msgDB);

        return msgId;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        chatMsgMapper.batchUpdateMsgSigned(msgIdList);
    }
}
