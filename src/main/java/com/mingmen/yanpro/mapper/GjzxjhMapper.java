package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.dao.GjzxjhDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GjzxjhMapper extends BaseMapper<GjzxjhDao> {

    @Select("select * from base_gjzxjh")
    List<GjzxjhDao> selectAll();

    @Select("select ZXJHDM,ZXJHMC,BZ from base_gjzxjh")
    List<GjzxjhDao> selectExport();

    @Insert("insert into base_gjzxjh(ZXJHDM, ZXJHMC, BZ) values (#{ZXJHDM}, #{ZXJHMC}, #{BZ})")
    int insert(GjzxjhDao gjzxjhDao);

    int update(GjzxjhDao gjzxjhDao);

    @Select("select count(*) from base_gjzxjh where ZXJHDM = #{ZXJHDM}")
    int selectZxjhdm(GjzxjhDao gjzxjhDao);

//    @Delete("delete from base_gjzxjh where ZXJHDM = #{ZXJHDM}")
//    Integer deleteByZXJHDM(@Param("ZXJHDM") String ZXJHDM);

    @Delete("delete from base_gjzxjh where ID = #{ID}")
    Integer deleteById(@Param("ID") Integer ID);

//    @Select("select * from base_gjzxjh limit #{pageNum}, #{pageSize}")
//    List<GjzxjhDao> selectPage(Integer pageNum, Integer pageSize);

    @Select("select * from base_gjzxjh where ZXJHDM like #{ZXJHDM} and ZXJHMC like #{ZXJHMC} limit #{pageNum}, #{pageSize}")
    List<GjzxjhDao> selectPage(Integer pageNum, Integer pageSize, String ZXJHDM, String ZXJHMC);

    @Select("select count(*) from base_gjzxjh where ZXJHDM like #{ZXJHDM} and ZXJHMC like #{ZXJHMC}")
    Integer selectTotal(@Param("ZXJHDM") String ZXJHDM,@Param("ZXJHMC") String ZXJHMC);

    @Select("select * from base_gjzxjh where ZXJHDM = #{ZXJHDM}")
    GjzxjhDao selectByZxjhdm(@Param("ZXJHDM") String ZXJHDM);

    @Select("select * from base_gjzxjh where ZXJHMC = #{ZXJHMC}")
    GjzxjhDao selectByZxjhmc(@Param("ZXJHMC") String ZXJHMC);

//    Page<GjzxjhDao> findPage(Page<GjzxjhDao> page,@Param("zxjhdm") String zxjhdm,@Param("zxjhmc") String zxjhmc);
}
