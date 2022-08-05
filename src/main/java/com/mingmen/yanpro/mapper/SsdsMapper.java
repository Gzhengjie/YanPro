package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.SsdsDao;
import com.mingmen.yanpro.dao.YXSDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SsdsMapper {

    @Select("SELECT * FROM base_daoshi_ss")
    List<SsdsDao> selectAll();

    @Select("SELECT * FROM base_daoshi_ss WHERE DSXM = #{DSXM}")
    List<SsdsDao> selectByMC(String DSXM);

    @Insert("INSERT INTO base_daoshi_ss(ZGH,DSXM,SZYXDM,SZYXMC,ZC,XSZSYXDM,XSZSYXMC,XSZSZYDM,XSZSZYMC,ZSZSYXDM,ZSZSYXMC,ZSZSZYDM,ZSZSZYMC,BZ,NF)" +
            " VALUES(#{ZGH},#{DSXM},#{SZYXDM},#{SZYXMC},#{ZC},#{XSZSYXDM},#{XSZSYXMC},#{XSZSZYDM},#{XSZSZYMC},#{ZSZSYXDM},#{ZSZSYXMC},#{ZSZSZYDM},#{ZSZSZYMC},#{BZ},#{NF})")
    int insert(SsdsDao yxsDao);

    int update(SsdsDao yxsDao);

    @Delete("Delete from base_daoshi_ss where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_daoshi_ss limit #{pageNum}, #{pageSize}")
    List<SsdsDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_daoshi_ss")
    Integer selectTotal();
}
