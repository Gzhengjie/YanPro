package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.RzDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RzMapper {

    @Select("select * from base_log")
    List<RzDao> selectAll();

    @Insert("insert into base_log(CZRYXM, CZLX) values (#{CZRYXM}, #{CZLX})")
    int insert(RzDao rzDao);

    int update(RzDao rzDao);

    @Select("select count(*) from base_log where CZRYXM = #{CZRYXM}")
    int selectCZRYXM(RzDao rzDao);

    @Delete("delete from base_log where CZRYXM = #{CZRYXM} and CZLX = #{CZLX}")
    Integer delete(@Param("CZRYXM") String ZXJHDM, @Param("CZLX") String CZLX);

    @Select("select * from base_log where CZRYXM like #{CZRYXM} limit #{pageNum}, #{pageSize}")
    List<RzDao> selectPage(Integer pageNum, Integer pageSize, String CZRYXM);

    @Select("select count(*) from base_log")
    Integer selectTotal(String CZRYXM);

    @Select("select * from base_log where CZRYXM = #{CZRYXM}")
    RzDao selectByCZRYXM(@Param("CZRYXM") String CZRYXM);
}
