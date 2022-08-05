package com.mingmen.yanpro.mapper;

import com.mingmen.yanpro.dao.FileDao;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.File;

@Mapper
public interface FileMapper extends BaseMapper<FileDao> {

//    @Insert("insert into sys_file(name, type, size, url, md5) values (#{name}, #{type}, #{size}, #{url}, #{md5})")
//    int insert(FileDao fileDao);
}
