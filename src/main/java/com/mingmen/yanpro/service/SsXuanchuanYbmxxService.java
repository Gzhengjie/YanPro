package com.mingmen.yanpro.service;

import com.mingmen.yanpro.dao.SsXuanchuanYbmxx;
import com.mingmen.yanpro.mapper.SsXuanchuanYbmxxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SsXuanchuanYbmxxService {

    @Autowired
    private SsXuanchuanYbmxxMapper ssXuanchuanYbmxxMapper;

    public int save(SsXuanchuanYbmxx ssXuanchuanYbmxx){
        if(ssXuanchuanYbmxx.getId() == null){ //没有id则表示是新增
            return ssXuanchuanYbmxxMapper.insert(ssXuanchuanYbmxx);
        } else {  //否则为更新
            return ssXuanchuanYbmxxMapper.update(ssXuanchuanYbmxx);
        }
    }
}
