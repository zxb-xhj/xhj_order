package com.xhj.service;

import com.xhj.dto.UserLoginDTO;
import com.xhj.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
