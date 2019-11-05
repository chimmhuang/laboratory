package com.chimm.muxin.domain.vo;

import lombok.Data;

/**
 * @author huangshuai
 * @date 2019/11/5
 */
@Data
public class UsersVO {
    private String id;
    private String username;
    private String faceImage;
    private String faceImageBig;
    private String nickname;
    private String qrCode;
}
