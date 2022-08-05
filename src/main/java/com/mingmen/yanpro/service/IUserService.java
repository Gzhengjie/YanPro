package com.mingmen.yanpro.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingmen.yanpro.controller.dto.SUserDTO;
import com.mingmen.yanpro.controller.dto.UserPasswordDTO;
import com.mingmen.yanpro.dao.SUser;


/**
 * <p>
 *  服务类
 * </p>
 */
public interface IUserService extends IService<SUser> {

    SUserDTO login(SUserDTO SUserDTO);

    SUser register(SUserDTO SUserDTO);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Page<SUser> findPage(Page<SUser> objectPage, String username, String email, String address);

    SUserDTO loginEmail(SUserDTO SUserDTO);

    void sendEmailCode(String email,Integer type);
}
