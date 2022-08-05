package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.SszszyDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SszszyMapper extends BaseMapper<SszszyDao> {

    @Select("SELECT * FROM base_zhuanye_ss")
    List<SszszyDao> selectAll();

    @Select("SELECT * FROM base_zhuanye_ss WHERE ZYMC = #{ZYMC}")
    List<SszszyDao> selectByMC(String ZYMC);

    @Insert("INSERT INTO base_zhuanye_ss(YXSDM,YXSMC,ZYDM,ZYMC,BZ,NF) VALUES(#{YXSDM},#{YXSMC},#{ZYDM},#{ZYMC},#{BZ},#{NF})")
    int insert(SszszyDao bszszyDao);

    int update(SszszyDao bszszyDao);

    @Delete("Delete from base_zhuanye_ss where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_zhuanye_ss limit #{pageNum}, #{pageSize}")
    List<SszszyDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_zhuanye_ss")
    Integer selectTotal();
}
