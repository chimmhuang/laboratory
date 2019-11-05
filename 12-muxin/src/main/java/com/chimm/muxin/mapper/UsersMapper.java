package com.chimm.muxin.mapper;

import com.chimm.muxin.domain.Users;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@Repository
public interface UsersMapper extends Mapper<Users> {
}
