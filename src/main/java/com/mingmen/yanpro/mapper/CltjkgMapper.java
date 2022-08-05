package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.CltjkgDao;
import com.mingmen.yanpro.dao.YXSDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CltjkgMapper {

    @Select("SELECT * FROM base_cltjkg")
    List<CltjkgDao> selectAll();

    @Select("SELECT * FROM base_cltjkg WHERE CLMC = #{CLMC}")
    List<CltjkgDao> selectByMC(String CLMC);

    @Insert("INSERT INTO base_cltjkg(CLMC) VALUES(#{CLMC})")
    int insert(CltjkgDao cltjkgDao);

    int update(CltjkgDao cltjkgDao);

    @Delete("Delete from base_cltjkg where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_cltjkg limit #{pageNum}, #{pageSize}")
    List<CltjkgDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_cltjkg")
    Integer selectTotal();
}
