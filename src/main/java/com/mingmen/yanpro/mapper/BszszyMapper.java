package com.mingmen.yanpro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.YXSDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BszszyMapper extends BaseMapper<BszszyDao> {

    @Select("SELECT * FROM base_zhuanye_bs")
    List<BszszyDao> selectAll();

    @Select("SELECT * FROM base_zhuanye_bs WHERE ZYMC = #{ZYMC}")
    List<BszszyDao> selectByMC(String ZYMC);

    @Insert("INSERT INTO base_zhuanye_bs(YXSDM,ZYDM,ZYMC,BZ,NF) VALUES(#{YXSDM},#{ZYDM},#{ZYMC},#{BZ},#{NF})")
    int insert(BszszyDao bszszyDao);

    int update(BszszyDao bszszyDao);

    @Delete("Delete from base_zhuanye_bs where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_zhuanye_bs limit #{pageNum}, #{pageSize}")
    List<BszszyDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_zhuanye_bs")
    Integer selectTotal();
}
