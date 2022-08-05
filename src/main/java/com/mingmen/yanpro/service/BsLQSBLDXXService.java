package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.*;
import com.mingmen.yanpro.mapper.BsLQSBLDXXMapper;
import com.mingmen.yanpro.mapper.BszszyMapper;
import com.mingmen.yanpro.mapper.CollegeMapper;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class BsLQSBLDXXService extends ServiceImpl<BsLQSBLDXXMapper, BsLQSBLDXX> {
    @Autowired
    private BsLQSBLDXXMapper bsLQSBLDXXMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private BszszyMapper bszszyMapper;

    public Result saveUser(BsLQSBLDXX bsLQSBLDXX) {
        if (!bsLQSBLDXXMapper.allow("博士硕博连读录取"))
            return Result.error(Constants.CODE_500, "不在提交时间范围");
        QueryWrapper<BsLQSBLDXX> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("xh", bsLQSBLDXX.getXh());
        BsLQSBLDXX bsLQSBLDXX1 = bsLQSBLDXXMapper.selectOne(queryWrapper1);
        if (bsLQSBLDXX1 != null)
            return Result.error(Constants.CODE_500, "用户已存在");
        if (bsLQSBLDXX.getId() == null) {
            bsLQSBLDXX.setScsj(DateUtil.today());
            bsLQSBLDXX.setScr(TokenUtils.getCurrentUser().getXM());
        }
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        bsLQSBLDXX.setYxsdm(TokenUtils.getCurrentUser().getYXSDM());
        queryWrapper.eq("yxsdm", TokenUtils.getCurrentUser().getYXSDM());
        bsLQSBLDXX.setYxsmc(collegeMapper.selectOne(queryWrapper).getYxsmc());
        bsLQSBLDXX.setZydm(bszszyMapper.selectByMC(bsLQSBLDXX.getZymc()).get(0).getZYDM());
        return Result.success(saveOrUpdate(bsLQSBLDXX));
    }


    public JSONArray findAlllqyxmc() {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = bsLQSBLDXXMapper.alllqyxmc();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findAlllqzymc(String yxsdm, String yxsmc, int nf) {
        if ("".equals(yxsdm))
            yxsdm = getYxsdm(yxsmc);
        if (nf == 0)
            nf = DateUtil.year(DateUtil.date());
        JSONArray jsonArray = new JSONArray();
        List<String> allname = bsLQSBLDXXMapper.alllqzymc(yxsdm, nf);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findAllssfx(@RequestParam String zymc) {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = bsLQSBLDXXMapper.allxxfs(zymc);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray tongJi(int nf) {
        if (nf == 0){
            nf = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = bsLQSBLDXXMapper.alllqyxmc();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("xy", name);
            jsonObject.putOpt("dsh", bsLQSBLDXXMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("yjsytg", bsLQSBLDXXMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("yjsybtg", bsLQSBLDXXMapper.totalSutShzt("", name, 5, nf));
            jsonObject.putOpt("nf", nf);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public HashMap<String, Integer> total(String lqyxdm, String lqyxmc, int nf) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        if (nf == 0){
            nf = DateUtil.year(DateUtil.date());
        }
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            hashMap.put("dshrs", bsLQSBLDXXMapper.totalSutShzt("", lqyxmc,2, nf));
            hashMap.put("shtgrs", bsLQSBLDXXMapper.totalSutShzt("", lqyxmc,4, nf));
            hashMap.put("shbtgrs", bsLQSBLDXXMapper.totalSutShzt("", lqyxmc,5, nf));
            return hashMap;
        }
        else if ("2-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            hashMap.put("dshrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",1, nf));
            hashMap.put("xyshtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",2, nf));
            hashMap.put("xyshbtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",3, nf));
            hashMap.put("yyshtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",4, nf));
            hashMap.put("yyshbtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",5, nf));
            return hashMap;
        }
        else {
            hashMap.put("dtj", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",0, nf));
            hashMap.put("ytj", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",1, nf));
            hashMap.put("xyshtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",2, nf));
            hashMap.put("xyshbtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",3, nf));
            hashMap.put("yyshtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",4, nf));
            hashMap.put("yyshbtgrs", bsLQSBLDXXMapper.totalSutShzt(yxsdm, "",5, nf));
            return hashMap;
        }
    }

    public JSONObject zxnf() {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<BsLQSBLDXX> queryWrapper = new QueryWrapper<>();
        jsonObject.putOpt("min", bsLQSBLDXXMapper.selectOne(queryWrapper.orderByDesc("nf").last("limit 1")).getNf());
        jsonObject.putOpt("max", DateUtil.year(DateUtil.date()));
        return jsonObject;
    }
    public String getYxsdm(String yxsmc) {
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("yxsmc", yxsmc);
        return collegeMapper.selectOne(queryWrapper).getYxsdm();
    }


    public Result saveBatch(List<BsLQSBLDXX> list, String yxsdm, String xm) {
        List<BsLQSBLDXX> err = new ArrayList<>();
        int i = 0;
        QueryWrapper<College> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("yxsdm", yxsdm);
        String yxsmc = collegeMapper.selectOne(queryWrapper3).getYxsmc();
        Pattern pattern = Pattern.compile("[0-9]*");
        for(BsLQSBLDXX one : list) {
            int flag = 0;
            if (!pattern.matcher(one.getXh()+"").matches() || one.getXh().length() != 10){
                one.setBz(one.getBz() + "错误信息：考生学号位数错误");
                flag = 1;
            }
            one.setYxsdm(yxsdm);
            one.setYxsmc(yxsmc);
            QueryWrapper<BszszyDao> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("yxsdm", one.getYxsdm());
            queryWrapper.eq("ZYDM", one.getZydm());
            queryWrapper.eq("ZYMC", one.getZymc());
            if (bszszyMapper.selectCount(queryWrapper) == 0){
                one.setBz(one.getBz()  + "错误信息：专业名称或专业代码错误");
                flag = 1;
            }
            QueryWrapper<BsLQSBLDXX> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("xh", one.getXh());
            if (bsLQSBLDXXMapper.selectCount(queryWrapper2) != 0)
            {
                one.setBz(one.getBz() + "错误信息：该考生已存在");
                flag = 1;
            }
            if (flag == 0){
                one.setScr(xm);
                one.setScsj(DateUtil.today());
                saveOrUpdate(one);
                i++;
            }
            else {
                err.add(one);
            }
        }
        JSONObject jsonObject = new JSONObject();
        if (err.size() == 0)
            jsonObject.putOpt("all", true);
        else
            jsonObject.putOpt("all", false);
        jsonObject.putOpt("success", i);
        jsonObject.putOpt("error", err);
        jsonObject.putOpt("errorcount", err.size());
        return Result.success(jsonObject);
    }
}
