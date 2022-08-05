package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.YXSDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface YXSMapper {

    @Select("SELECT * FROM base_college")
    List<YXSDao> selectAll();

    @Select("SELECT * FROM base_college WHERE YXSMC = #{YXSMC}")
    List<YXSDao> selectByMC(String YXSMC);

    @Insert("INSERT INTO base_college(YXSDM,YXSMC,BZ) VALUES(#{YXSDM},#{YXSMC},#{BZ})")
    int insert(YXSDao yxsDao);

    int update(YXSDao yxsDao);

    @Delete("Delete from base_college where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_college limit #{pageNum}, #{pageSize}")
    List<YXSDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_college")
    Integer selectTotal();
}
