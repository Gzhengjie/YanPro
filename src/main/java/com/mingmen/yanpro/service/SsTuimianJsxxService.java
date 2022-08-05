package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.dao.SsTuimianJsxx;
import com.mingmen.yanpro.mapper.SsTuimianJsxxMapper;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SsTuimianJsxxService extends ServiceImpl<SsTuimianJsxxMapper, SsTuimianJsxx> {

    @Autowired
    private SsTuimianJsxxMapper ssTuimianJsxxMapper;

    public boolean saveSsTuimianJsxx(SsTuimianJsxx ssTuimianJsxx) {

        return saveOrUpdate(ssTuimianJsxx);
    }

    @Override
    public List<SsTuimianJsxx> list()
    {
        return  ssTuimianJsxxMapper.selectAll();
    }

    public IPage<SsTuimianJsxx> findPage(Page<SsTuimianJsxx> page, String tjyxsdm, String tjyxsmc, String dsxm, int nf, int shzt)
    {
        if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            return ssTuimianJsxxMapper.findPage(page, tjyxsdm, tjyxsmc, dsxm, nf, shzt);
        }
        else {
            return ssTuimianJsxxMapper.findPage(page, TokenUtils.getCurrentUser().getYXSDM(), tjyxsmc, dsxm, nf, shzt);
        }


    }


    public JSONArray tongJi(int nf) {
        if (nf == 0){
            nf = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssTuimianJsxxMapper.alltjyxsmc();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("xy", name);
            jsonObject.putOpt("dsh", ssTuimianJsxxMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("yjsytg", ssTuimianJsxxMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("yjsybtg", ssTuimianJsxxMapper.totalSutShzt("", name, 5, nf));
            jsonObject.putOpt("nf", nf);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

//    @Autowired
//    private SsTuimianJsxxMapper ssTuimianJsxxMapper;
//
//
//    public int save(SsTuimianJsxx ssTuimianJsxx){
//
//        if(ssTuimianJsxx.getId() == null){ //没有id则表示是新增
//            return ssTuimianJsxxMapper.insert(ssTuimianJsxx);
//        } else {  //否则为更新
//            return ssTuimianJsxxMapper.update(ssTuimianJsxx);
//        }
//    }

}
