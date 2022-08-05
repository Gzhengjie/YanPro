package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.BszzbfpDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BszzbfpMapper {

    @Select("SELECT * FROM bs_jianzhang_zzbfp")
    List<BszzbfpDao> selectAll();

    @Select("SELECT * FROM bs_jianzhang_zzbfp WHERE YXSMC = #{YXSMC}")
    List<BszzbfpDao> selectByMC(String YXSMC);

    @Insert("INSERT INTO bs_jianzhang_zzbfp(YXSDM,YXSMC,ZSSL,ZSZL,TMSL,TMZL,NF,SCR) VALUES(#{YXSDM},#{YXSMC},#{ZSSL},#{ZSZL},#{TMSL},#{TMZL},#{NF},#{SCR})")
    int insert(BszzbfpDao bszzbfpDao);

    int update(BszzbfpDao bszzbfpDao);

    @Delete("Delete from bs_jianzhang_zzbfp where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from bs_jianzhang_zzbfp limit #{pageNum}, #{pageSize}")
    List<BszzbfpDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from bs_jianzhang_zzbfp")
    Integer selectTotal();
}
