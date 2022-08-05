package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.WYDJDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WYDJMapper {
    //    选择所有数据
    @Select("SELECT * FROM base_wydj")
    List<WYDJDao> selectAll();

    //    分页查找
    @Select("select * from  base_wydj where TGXMMC like concat('%', #{mc}, '%') limit #{pageNum}, #{pageSize}")
    List<WYDJDao> selectPage(Integer pageNum, Integer pageSize, String mc);

    //    插入数据
    @Insert("INSERT INTO base_wydj(TGXMDM,TGXMMC) VALUES(#{TGXMDM},#{TGXMMC})")
    Integer insert(WYDJDao wydjDao);

    //    查找
    @Select("SELECT * FROM base_wydj WHERE TGXMDM = #{TGXMDM}")
    WYDJDao selectByTGXMDM(@Param("TGXMDM") String TGXMDM);

    //    查找
    @Select("SELECT * FROM base_wydj WHERE TGXMMC = #{TGXMMC}")
    WYDJDao selectByTGXMMC(@Param("TGXMMC") String TGXMMC);


    //    删除
    @Delete("Delete from base_wydj where ID = #{ID}")
    Integer delete(@Param("ID") Integer ID);



    //    更新
    Integer update(WYDJDao wydjDao);

    //    查询总个数
    @Select("select count(*) from base_wydj where TGXMMC like concat('%', #{mc}, '%')")
    Integer selectTotal(String mc);
}
