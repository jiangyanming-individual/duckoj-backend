package com.jiang.duckoj.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新请求，管理员
 *
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;


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
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}