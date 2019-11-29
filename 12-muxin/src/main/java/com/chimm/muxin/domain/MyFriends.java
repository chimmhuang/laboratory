package com.chimm.muxin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "my_friends")
public class MyFriends {
      @Id
      private String id;
      private String myUserId;
      private String myFriendUserId;
}
