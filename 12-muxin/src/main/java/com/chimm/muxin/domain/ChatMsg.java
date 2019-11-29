package com.chimm.muxin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_msg")
public class ChatMsg {
      @Id
      private String id;
      private String sendUserId;
      private String acceptUserId;
      private String msg;
      private Integer signFlag;
      private Date createTime;
}
