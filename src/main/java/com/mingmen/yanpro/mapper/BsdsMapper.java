package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.BsdsDao;
import com.mingmen.yanpro.dao.SsdsDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BsdsMapper {

    @Select("SELECT * FROM base_daoshi_bs")
    List<BsdsDao> selectAll();

    @Select("SELECT * FROM base_daoshi_bs WHERE DSXM = #{DSXM}")
    List<BsdsDao> selectByMC(String DSXM);

    @Insert("INSERT INTO base_daoshi_bs(ZGH,DSXM,SZYXDM,SZYXMC,ZC,XBZSYXDM,XBZSYXMC,XBZSZYDM,XBZSZYMC,ZBZSYXDM,ZBZSYXMC,ZBZSZYDM,ZBZSZYMC,BZ,NF)" +
            " VALUES(#{ZGH},#{DSXM},#{SZYXDM},#{SZYXMC},#{ZC},#{XBZSYXDM},#{XBZSYXMC},#{XBZSZYDM},#{XBZSZYMC},#{ZBZSYXDM},#{ZBZSYXMC},#{ZBZSZYDM},#{ZBZSZYMC},#{BZ},#{NF})")
    int insert(BsdsDao yxsDao);

    int update(BsdsDao yxsDao);

    @Delete("Delete from base_daoshi_bs where ID = #{ID}")
    Integer deleteByID(@Param("ID") String ID);


    @Select("select * from base_daoshi_bs limit #{pageNum}, #{pageSize}")
    List<BsdsDao> selectPage(Integer pageNum,Integer pageSize);

    @Select("select count(*) from base_daoshi_bs")
    Integer selectTotal();
}
