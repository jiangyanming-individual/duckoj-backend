package com.jiang.duckoj.model.dto.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户创建请求
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;


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
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}