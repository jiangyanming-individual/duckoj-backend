package com.jiang.duckoj.model.dto.user;

import com.jiang.duckoj.common.PageRequest;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;


    /**
     * 性别
     */
    private String gender;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态：
     */
    private String userState;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}