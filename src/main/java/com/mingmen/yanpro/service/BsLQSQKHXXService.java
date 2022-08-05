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
import com.mingmen.yanpro.mapper.BsLQSQKHXXMapper;
import com.mingmen.yanpro.mapper.BszszyMapper;
import com.mingmen.yanpro.mapper.CollegeMapper;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class BsLQSQKHXXService extends ServiceImpl<BsLQSQKHXXMapper, BsLQSQKHXX> {

    @Autowired
    private BsLQSQKHXXMapper bsLQSQKHXXMapper;
    @Autowired
    private BsLQSBLDXXMapper bsLQSBLDXXMapper;

    @Autowired
    private BszszyMapper bszszyMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    public Result saveUser(BsLQSQKHXX bsLQSQKHXX) {
        if (!bsLQSQKHXXMapper.allow("博士申请考核录取"))
            return Result.error(Constants.CODE_500, "不在提交时间范围");
        QueryWrapper<BsLQSQKHXX> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("zjhm", bsLQSQKHXX.getZjhm());
        queryWrapper1.eq("nf", bsLQSQKHXX.getNf());
        BsLQSQKHXX bsLQSQKHXX1 = bsLQSQKHXXMapper.selectOne(queryWrapper1);
        if (bsLQSQKHXX1 != null)
            return Result.error(Constants.CODE_500, "用户已存在");
        if (bsLQSQKHXX.getId() == null) {
            bsLQSQKHXX.setScsj(DateUtil.today());
            bsLQSQKHXX.setScr(TokenUtils.getCurrentUser().getXM());
        }
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        bsLQSQKHXX.setYxsdm(TokenUtils.getCurrentUser().getYXSDM());
        queryWrapper.eq("yxsdm", TokenUtils.getCurrentUser().getYXSDM());
        bsLQSQKHXX.setYxsmc(collegeMapper.selectOne(queryWrapper).getYxsmc());
        bsLQSQKHXX.setZydm(bszszyMapper.selectByMC(bsLQSQKHXX.getZymc()).get(0).getZYDM());
        return Result.success(saveOrUpdate(bsLQSQKHXX));
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
            jsonObject.putOpt("dsh", bsLQSQKHXXMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("yjsytg", bsLQSQKHXXMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("yjsybtg", bsLQSQKHXXMapper.totalSutShzt("", name, 5, nf));
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
            hashMap.put("dshrs", bsLQSQKHXXMapper.totalSutShzt("", lqyxmc,2, nf));
            hashMap.put("shtgrs", bsLQSQKHXXMapper.totalSutShzt("", lqyxmc,4, nf));
            hashMap.put("shbtgrs", bsLQSQKHXXMapper.totalSutShzt("", lqyxmc,5, nf));
            return hashMap;
        }
        else if ("2-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            hashMap.put("dshrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",1, nf));
            hashMap.put("xyshtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",2, nf));
            hashMap.put("xyshbtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",3, nf));
            hashMap.put("yyshtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",4, nf));
            hashMap.put("yyshbtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",5, nf));
            return hashMap;
        }
        else {
            hashMap.put("dtj", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",0, nf));
            hashMap.put("ytj", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",1, nf));
            hashMap.put("xyshtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",2, nf));
            hashMap.put("xyshbtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",3, nf));
            hashMap.put("yyshtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",4, nf));
            hashMap.put("yyshbtgrs", bsLQSQKHXXMapper.totalSutShzt(yxsdm, "",5, nf));
            return hashMap;
        }
    }
    public JSONObject zxnf() {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<BsLQSQKHXX> queryWrapper = new QueryWrapper<>();
        jsonObject.putOpt("min", bsLQSQKHXXMapper.selectOne(queryWrapper.orderByDesc("nf").last("limit 1")).getNf());
        jsonObject.putOpt("max", DateUtil.year(DateUtil.date()));
        return jsonObject;
    }
    public Result saveBatch(List<BsLQSQKHXX> list, String yxsdm, String xm) {
        List<BsLQSQKHXX> err = new ArrayList<>();
        int i = 0;
        QueryWrapper<College> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("yxsdm", yxsdm);
        String yxsmc = collegeMapper.selectOne(queryWrapper3).getYxsmc();
        Pattern pattern = Pattern.compile("[0-9]*");
        for(BsLQSQKHXX one : list) {
            int flag = 0;
            if (!pattern.matcher(one.getDszgh()+"").matches() || one.getDszgh().length() != 10 || one.getZjhm().length() != 18){
                one.setBz(one.getBz() + "错误信息：考生编号位数错误或导师职工号错误");
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
            QueryWrapper<BsLQSQKHXX> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("zjhm", one.getZjhm());
            queryWrapper2.eq("nf", one.getNf());
            if (bsLQSQKHXXMapper.selectCount(queryWrapper2) != 0)
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
