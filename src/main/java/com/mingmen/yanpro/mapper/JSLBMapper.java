package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.JSLBDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JSLBMapper {
    //    选择所有数据
    @Select("SELECT * FROM base_jslx")
    List<JSLBDao> selectAll();

    //    分页查找
    @Select("select * from  base_jslx where JSLBMC like concat('%', #{mc}, '%') limit #{pageNum}, #{pageSize}")
    List<JSLBDao> selectPage(Integer pageNum, Integer pageSize, String mc);

    //    插入数据
    @Insert("INSERT INTO base_jslx(JSLBDM,JSLBMC) VALUES(#{JSLBDM},#{JSLBMC})")
    Integer insert(JSLBDao jslbDao);

    //    查找
    @Select("SELECT * FROM base_jslx WHERE JSLBDM = #{JSLBDM}")
    JSLBDao selectByJSLBDM(@Param("JSLBDM") String JSLBDM);

    //    查找
    @Select("SELECT * FROM base_jslx WHERE JSLBMC = #{JSLBMC}")
    JSLBDao selectByJSLBMC(@Param("JSLBMC") String JSLBMC);

    //    删除
    @Delete("Delete from base_jslx where ID = #{ID}")
    Integer delete(@Param("ID") Integer ID);

    //    更新
    Integer update(JSLBDao jslbDao);

    //    查询总个数
    @Select("select count(*) from base_jslx where JSLBMC like concat('%', #{mc}, '%')")
    Integer selectTotal(String mc);

}
