package com.chimm.muxin.mapper;

import com.chimm.muxin.domain.Users;
import com.chimm.muxin.domain.vo.FriendRequestVO;
import com.chimm.muxin.domain.vo.MyFriendsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@Repository
public interface UsersMapper extends Mapper<Users> {

    List<FriendRequestVO> queryFriendRequestList(@Param("acceptUserId") String acceptUserId);

    List<MyFriendsVO> queryMyFriends(@Param("userId") String userId);

    int batchUpdateMsgSigned(List list);
}
