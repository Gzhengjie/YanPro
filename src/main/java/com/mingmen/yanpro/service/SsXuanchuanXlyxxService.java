package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsXuanchuanXlyxx;
import com.mingmen.yanpro.dao.Zyzsxx;
import com.mingmen.yanpro.mapper.SsXuanchuanXlyxxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mingmen.yanpro.utils.JianzhangUtils.str2list;

@Service
public class  SsXuanchuanXlyxxService extends ServiceImpl<SsXuanchuanXlyxxMapper,SsXuanchuanXlyxx> {

    @Autowired
    private SsXuanchuanXlyxxMapper ssXuanchuanXlyxxMapper;

    public Integer saveSsXuanchuanXlyxx(SsXuanchuanXlyxx ssXuanchuanXlyxx)
    {
        if(ssXuanchuanXlyxx.getID()==null){
            return ssXuanchuanXlyxxMapper.insert(ssXuanchuanXlyxx);
        }else {
            return ssXuanchuanXlyxxMapper.update(ssXuanchuanXlyxx);
        }
    }

    public JSONArray tongJi(int nf) {
        if (nf == 0){
            nf = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssXuanchuanXlyxxMapper.allyxsmc();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("xy", name);
            jsonObject.putOpt("dsh", ssXuanchuanXlyxxMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("yjsytg", ssXuanchuanXlyxxMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("yjsybtg", ssXuanchuanXlyxxMapper.totalSutShzt("", name, 5, nf));
            jsonObject.putOpt("nf", nf);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public List<SsXuanchuanXlyxx> selectAll(){ return ssXuanchuanXlyxxMapper.selectAll(); }

    public IPage<SsXuanchuanXlyxx> findPage(Page<SsXuanchuanXlyxx> page, String EMAIL, Integer NF, String YXSDM, Integer SHZT, String XM, String ZJHM) {
        if (NF == 0) {
            NF = DateUtil.year(DateUtil.date());
        }
        return ssXuanchuanXlyxxMapper.findPage(page,EMAIL,NF,YXSDM,SHZT,XM,ZJHM);

    }

    public SsXuanchuanXlyxx selectById(Integer ID) {return ssXuanchuanXlyxxMapper.selectById(ID);
    }

/*
    public int save(SsXuanchuanXlyxx ssXuanchuanXlyxx){
        if(ssXuanchuanXlyxx.getId() == null){ //没有id则表示是新增
            return ssXuanchuanXlyxxMapper.insert(ssXuanchuanXlyxx);
        } else {  //否则为更新
            return ssXuanchuanXlyxxMapper.update(ssXuanchuanXlyxx);
        }
    }
*/
}
