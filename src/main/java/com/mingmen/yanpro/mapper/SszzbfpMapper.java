package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.BszzbfpDao;
import com.mingmen.yanpro.dao.SszzbfpDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SszzbfpMapper {

    @Select("SELECT * FROM ss_jianzhang_zzbfp")
    List<SszzbfpDao> selectAll();

    @Select("SELECT * FROM ss_jianzhang_zzbfp WHERE YXSMC = #{YXSMC}")
    List<SszzbfpDao> selectByMC(String YXSMC);

    @Insert("INSERT INTO ss_jianzhang_zzbfp(YXSDM,YXSMC,ZSSL,ZSZL,TMSL,TMZL,NF,SCR) VALUES(#{YXSDM},#{YXSMC},#{ZSSL},#{ZSZL},#{TMSL},#{TMZL},#{NF},#{SCR})")
    int insert(SszzbfpDao sszzbfpDao);

    int update(SszzbfpDao sszzbfpDao);

    @Delete("Delete from ss_jianzhang_zzbfp where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from ss_jianzhang_zzbfp limit #{pageNum}, #{pageSize}")
    List<SszzbfpDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from ss_jianzhang_zzbfp")
    Integer selectTotal();
}
