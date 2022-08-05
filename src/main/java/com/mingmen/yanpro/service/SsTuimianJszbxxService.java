package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.SsTuimianJszbxx;
import com.mingmen.yanpro.mapper.SsTuimianJszbxxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SsTuimianJszbxxService extends ServiceImpl<SsTuimianJszbxxMapper, SsTuimianJszbxx> {

    @Autowired
    private SsTuimianJszbxxMapper ssTuimianJszbxxMapper;

    public boolean saveSsTuimianJszbxx(SsTuimianJszbxx ssTuimianJszbxx){
        return saveOrUpdate(ssTuimianJszbxx);
    }

    public JSONArray tongJi(int nf) {
        if (nf == 0){
            nf = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssTuimianJszbxxMapper.alljsyxsdm();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("xy", name);
            jsonObject.putOpt("dsh", ssTuimianJszbxxMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("yjsytg",ssTuimianJszbxxMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("yjsybtg", ssTuimianJszbxxMapper.totalSutShzt("", name, 5, nf));
            jsonObject.putOpt("nf", nf);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
