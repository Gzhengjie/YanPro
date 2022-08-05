package com.mingmen.yanpro.mapper;


import com.mingmen.yanpro.dao.PMFSDao;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PMFSMapper {
    //    选择所有数据
    @Select("SELECT * FROM base_pmfs")
    List<PMFSDao> selectAll();

    //    分页查找
    @Select("select * from  base_pmfs where PMFSMC like concat('%', #{mc}, '%') limit #{pageNum}, #{pageSize}")
    List<PMFSDao> selectPage(Integer pageNum, Integer pageSize, String mc);

    //    插入数据
    @Insert("INSERT INTO base_pmfs(PMFSDM,PMFSMC) VALUES(#{PMFSDM},#{PMFSMC})")
    int insert(PMFSDao pmfsDao);

    //    查找
    @Select("SELECT * FROM base_pmfs WHERE PMFSDM = #{PMFSDM}")
    PMFSDao selectByPMFSDM(@Param("PMFSDM") String PMFSDM);

    //    查找
    @Select("SELECT * FROM base_pmfs WHERE PMFSMC = #{PMFSMC}")
    PMFSDao selectByPMFSMC(@Param("PMFSMC") String PMFSMC);

    //    删除

    @Delete("Delete from base_pmfs where ID = #{ID}")
    Integer delete(@Param("ID") Integer ID);

    //    更新
    Integer update(PMFSDao pmfsDao);

    //    查询总个数
    @Select("select count(*) from base_pmfs where PMFSMC like concat('%', #{mc}, '%')")
    Integer selectTotal(String mc);


}
