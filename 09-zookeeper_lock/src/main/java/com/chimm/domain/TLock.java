package com.chimm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * @author huangshuai
 * @date 2019-08-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_lock")
public class TLock {
    private Integer id;
}
