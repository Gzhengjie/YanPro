package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.TJDWDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TJDWMapper {
    //    选择所有数据
    @Select("SELECT * FROM base_tjdw")
    List<TJDWDao> selectAll();

    //    分页查找
    @Select("select * from  base_tjdw where TJDWMC like concat('%', #{mc}, '%') limit #{pageNum}, #{pageSize}")
    List<TJDWDao> selectPage(Integer pageNum, Integer pageSize, String mc);

    //    插入数据
    @Insert("INSERT INTO base_tjdw(TJDWDM,TJDWMC) VALUES(#{TJDWDM},#{TJDWMC})")
    Integer insert(TJDWDao tjdwDao);

    //    查找
    @Select("SELECT * FROM base_tjdw WHERE TJDWDM = #{TJDWDM}")
    TJDWDao selectByTJDWDM(@Param("TJDWDM") String TJDWDM);

    //    查找
    @Select("SELECT * FROM base_tjdw WHERE TJDWMC = #{TJDWMC}")
    TJDWDao selectByTJDWMC(@Param("TJDWMC") String TJDWMC);


    //    删除
    @Delete("Delete from base_tjdw where ID = #{ID}")
    Integer delete(@Param("ID") Integer ID);

    //    更新
    Integer update(TJDWDao tjdwDao);

    //    查询总个数
    @Select("select count(*) from base_tjdw where TJDWMC like concat('%', #{mc}, '%')")
    Integer selectTotal(String mc);

}
