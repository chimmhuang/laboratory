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
@Table(name = "friends_request")
public class FriendsRequest {
      @Id
      private String id;
      private String sendUserId;
      private String acceptUserId;
      private Date requestDateTime;
}
