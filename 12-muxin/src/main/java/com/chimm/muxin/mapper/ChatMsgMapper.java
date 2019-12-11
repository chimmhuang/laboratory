package com.chimm.muxin.mapper;

import com.chimm.muxin.domain.ChatMsg;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@Repository
public interface ChatMsgMapper extends Mapper<ChatMsg> {

    public void batchUpdateMsgSigned(List<String> msgIdList);
}
