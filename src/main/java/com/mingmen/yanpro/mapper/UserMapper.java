package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.UserDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserDao> {

    @Select("select * from base_user")
    List<UserDao> selectAll();

    @Insert("insert into base_user(ZGH, XM, YXSDM, MM, YHZ, CJR) values (#{ZGH}, #{XM}, #{YXSDM}, #{MM}, #{YHZ}, #{CJR})")
    int insert(UserDao userDao);

    int update(UserDao userDao);

//    @Delete("delete from base_user where ZGH = #{ZGH}")
//    Integer delete(@Param("ZGH") String ZGH);

    @Delete("delete from base_user where ID = #{ID}")
    Integer deleteById(@Param("ID") Integer ID);

    @Select("select * from base_user where XM like #{XM} and ZGH like #{ZGH} and YXSDM like #{YXSDM} and YHZ like #{YHZ} limit #{pageNum}, #{pageSize}")
    List<UserDao> selectPage(Integer pageNum, Integer pageSize, String XM, String ZGH, String YXSDM, String YHZ);

    @Select("select count(*) from base_user where XM like #{XM} and ZGH like #{ZGH} and YXSDM like #{YXSDM} and YHZ like #{YHZ}")
    Integer selectTotal(String XM, String ZGH, String YXSDM, String YHZ);

    @Select("select * from base_user where XM = #{XM}")
    UserDao selectByXM(@Param("XM") String XM);

    int updateYHZ(UserDao userDao);

    @Select("select * from base_user where ZGH = #{ZGH}")
    UserDao selectByZGH(@Param("ZGH") String ZGH);

    @Select("select count(*) from base_user where ZGH = #{ZGH} and MM=#{MM}")
    Integer existzgh(UserDao userDao);

    @Select("select base_college.YXSMC from base_user,base_college where base_user.ZGH = #{ZGH} AND base_user.YXSDM = base_college.YXSDM")
    String selectYxsmc(UserDao one);
}
