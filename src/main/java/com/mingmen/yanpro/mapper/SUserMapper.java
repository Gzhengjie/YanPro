package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.controller.dto.UserPasswordDTO;
import com.mingmen.yanpro.dao.SUser;
import com.mingmen.yanpro.dao.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 青哥哥
 * @since 2022-01-26
 */
public interface SUserMapper extends BaseMapper<SUser> {

    @Update("update sys_user set password = ``#{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    Page<SUser> findPage(Page<SUser> page, @Param("username") String username, @Param("email") String email, @Param("address") String address);
}
