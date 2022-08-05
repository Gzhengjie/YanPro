package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.SljtgqkDao;
import org.apache.ibatis.annotations.*;

import  java.util.List;

@Mapper
public interface SljtgqkMapper {

    @Select("select * from base_wydj")
    List<SljtgqkDao> findAll();

    @Insert("insert into base_wydj(TGXMDM, TGXMMC) values (#{TGXMDM}, #{TGXMMC})")
    int insert(SljtgqkDao sljtgqkDao);


    int update(SljtgqkDao sljtgqkDao);

    @Delete("delete from base_wydj where TGXMDM = #{TGXMDM}")
    Integer deleteByTgxmdm(@Param("TGXMDM") String TGXMDM);

    @Delete("delete from base_wydj where ID = #{ID}")
    Integer deleteById(@Param("ID") Integer ID);

    @Select("select count(*) from base_wydj where TGXMDM = #{TGXMDM}")
    int selectTgxmdm(SljtgqkDao sljtgqkDao);

    @Select("select * from base_wydj where TGXMDM like #{TGXMDM} and TGXMMC like #{TGXMMC} limit #{pageNum}, #{pageSize}")
    List<SljtgqkDao> selectPage(Integer pageNum, Integer pageSize, String TGXMDM, String TGXMMC);

    @Select("select count(*) from base_wydj where TGXMDM like #{TGXMDM} and TGXMMC like #{TGXMMC}")
    Integer selectTotal(String TGXMDM, String TGXMMC);

    @Select("select * from base_wydj where TGXMMC = #{TGXMMC}")
    SljtgqkDao selectByTgxmmc(String TGXMMC);
}
