package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.RoleDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("select * from base_role")
    List<RoleDao> selectAll();

    @Insert("insert into base_role(name, description, flag) values (#{name}, #{description}, #{flag})")
    int insert(RoleDao roleDao);

    int update(RoleDao roleDao);

    @Delete("delete from base_role where ID = #{ID}")
    Integer delete(@Param("ID") Integer id);

    @Select("select * from base_role where name like #{name} limit #{pageNum}, #{pageSize}")
    List<RoleDao> selectPage(Integer pageNum, Integer pageSize, @Param("name") String name);

    @Select("select count(*) from base_role where name like #{name}")
    Integer selectTotal(@Param("name") String name);

}
