package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.TJLXDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TJLXMapper {


    //    选择所有数据
    @Select("SELECT * FROM tuimian_type")
    List<TJLXDao> selectAll();

    //    分页查找
    @Select("select * from  tuimian_type where TJLXMC like concat('%', #{mc}, '%') limit #{pageNum}, #{pageSize}")
    List<TJLXDao> selectPage(Integer pageNum,Integer pageSize, String mc);

    //    插入数据
    @Insert("INSERT INTO tuimian_type(TJLXM,TJLXMC,BZ) VALUES(#{TJLXM},#{TJLXMC},#{BZ})")
    Integer insert(TJLXDao tjlxDao);

    //    按照推荐类型码查找
    @Select("SELECT * FROM tuimian_type WHERE TJLXM = #{TJLXM}")
    TJLXDao selectByTJLXM(@Param("TJLXM") String TJLXM);

    //    按照推荐类型码查找
    @Select("SELECT * FROM tuimian_type WHERE TJLXMC = #{TJLXMC}")
    TJLXDao selectByTJLXMC(@Param("TJLXMC") String TJLXMC);


    //    删除
    @Delete("Delete from tuimian_type where ID = #{ID}")
    Integer delete(@Param("ID") Integer ID);


    //    更新
    Integer update(TJLXDao tjlxDao);

    //    查询总个数
    @Select("select count(*) from tuimian_type where TJLXMC like concat('%', #{mc}, '%')")
    Integer selectTotal(String mc);
}
