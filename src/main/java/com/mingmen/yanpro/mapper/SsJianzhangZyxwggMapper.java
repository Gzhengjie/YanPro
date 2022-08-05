package com.mingmen.yanpro.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingmen.yanpro.dao.SsJianZhangZyxwgg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SsJianzhangZyxwggMapper extends BaseMapper<SsJianZhangZyxwgg> {
    @Update("UPDATE ss_jianzhang_zyxwgg SET BZ = #{bz} WHERE ID = #{id}")
    int myUpdate(String bz, int id);
}
