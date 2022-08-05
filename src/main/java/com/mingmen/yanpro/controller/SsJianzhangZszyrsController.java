package com.mingmen.yanpro.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsJianzhangZszyrs;
import com.mingmen.yanpro.dao.SsJianzhangZykskm;
import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.service.SsJianzhangZszyrsService;
import com.mingmen.yanpro.service.SsjianzhangZykskmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping("/ss_jianzhang_zszyrs")
public class SsJianzhangZszyrsController {


    @Autowired
    private SsJianzhangZszyrsService ssJianzhangZszyrsService;

    @Autowired
    private SsjianzhangZykskmService ssjianzhangZykskmService;


    // 获取所有学院的名称，用于前端查询
    @GetMapping("/allCollege")
    public Result getAllCollege() {
        return Result.success(ssjianzhangZykskmService.getAllCollege());
    }

    // 选了学院之后，在后一个下拉框内显示专业，这个查的不是基础表，是从zszyrs表里查出来的，等于是你没有插入，就没有这个专业
    @GetMapping("/college2zy")
    public Result college2zy(@RequestParam String YXSDM) {
        List<String> zy = new ArrayList<>();
        QueryWrapper<SsJianzhangZszyrs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", YXSDM);
        List<SsJianzhangZszyrs> temp = ssJianzhangZszyrsService.list(queryWrapper);
        for (SsJianzhangZszyrs ssJianzhangZszyrs : temp) {
            zy.add(ssJianzhangZszyrs.getZymc());
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(zy);

        return Result.success(new ArrayList<>(hashSet));
    }

    // 选了学院和专业之后，在第三个下拉框内显示方向
    @GetMapping("/zy2fx")
    public Result zy2fx(@RequestParam String YXSDM, @RequestParam String ZYMC) {
        List<String> zy = new ArrayList<>();
        QueryWrapper<SsJianzhangZszyrs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", YXSDM);
        queryWrapper.eq("ZYMC", ZYMC);
        List<SsJianzhangZszyrs> temp = ssJianzhangZszyrsService.list(queryWrapper);
        for (SsJianzhangZszyrs ssJianzhangZszyrs : temp) {
            zy.add(ssJianzhangZszyrs.getYjfxmc());
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(zy);

        return Result.success(new ArrayList<>(hashSet));
    }

    @GetMapping
    public Result index() {
        return Result.success(ssJianzhangZszyrsService.list());
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(ssJianzhangZszyrsService.removeById(id));
    }

    // 批量删除 - mybatis-plus的方式
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(ssJianzhangZszyrsService.removeByIds(ids));
    }


    // 保存或更新
    @PostMapping
    public Result save(@RequestBody SsJianzhangZszyrs ssJianzhangZszyrs) {

        return Result.success(ssJianzhangZszyrsService.saveSsJianzhangZszyrs(ssJianzhangZszyrs));
    }



    //分页查询 - mybatis-plus的方式
    @GetMapping("/page")
    public IPage<SsJianzhangZszyrs> findPage(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize,
                                             @RequestParam String YXSDM, // 院系所代码
                                             @RequestParam String ZYMC,   // 专业名称
                                             @RequestParam String YJFXMC,     // 研究方向名称
                                             @RequestParam String NF) {       // 年份
        IPage<SsJianzhangZszyrs> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SsJianzhangZszyrs> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("YXSDM", YXSDM);
        queryWrapper.like("ZYMC", ZYMC);
        queryWrapper.like("YJFXMC", YJFXMC);
        queryWrapper.like("NF", NF);
        queryWrapper.orderByDesc("ID");

        return ssJianzhangZszyrsService.page(page, queryWrapper);
    }



    /**
     * excel 导入
     * @param userFileReq
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(UserFileReq userFileReq) throws Exception{
        InputStream inputStream = userFileReq.getFile().getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<SsJianzhangZszyrs> list = reader.readAll(SsJianzhangZszyrs.class);
        inputStream.close();
        List<SsJianzhangZszyrs> ssJianzhangZszyrsList = new ArrayList<>();
        for(SsJianzhangZszyrs temp : list){
            temp.setScr(userFileReq.getXm());
            temp.setYxsdm(userFileReq.getYxsdm());
            temp.setYxsmc(ssjianzhangZykskmService.yxsdm2yxsmc(userFileReq.getYxsdm()));
            temp.setNf(userFileReq.getNf());
            ssJianzhangZszyrsList.add(temp);
        }
        int itemCount = list.size();
        List<SsJianzhangZszyrs> failList = ssJianzhangZszyrsService.saveWihtCheck(ssJianzhangZszyrsList);
        if(failList.size()!=0){
            return Result.error("600","共上传" + itemCount + "条数据，其中" + StrUtil.toString(itemCount- failList.size()) +
                    "条数据上传成功，" + failList.size() + "条数据上传失败。失败数据已下载，请更正后重新提交。",failList);
        } else {
            return Result.success("200","成功上传" + itemCount + "条数据",true);
        }
    }

    //硕士简章编制提交前，校验总人数是否与硕士总指标分配表里的总招生人数一致，并且总推免生人数不能少于总指标分配表里的总推免生人数
    //错误类型:1. 当年招生指标未分配 2. 总人数不一致 3. 推免人数少了 4. 提交的推免生人数+公开招考人数 不等于 拟招生人数 5. 学院招生专业人数表里无数据，请先维护
    //成功类型：0.允许提交
    //预提交不校验，正式提交的时候校验
    @GetMapping("/jianzhang_rs_jiaoyan")
    public Result jianZhangRsJiaoYan(@RequestParam String yxsdm, @RequestParam String nf){
        String res = ssJianzhangZszyrsService.jianZhangRsJiaoYan(yxsdm, nf);
        switch (res) {
            case "1":
                return Result.error("600", nf + "年招生指标未分配，无法提交", false);
            case "2":
                return Result.error("600", "拟招生人数与研究生院下发总指标不一致", false);
            case "3":
                return Result.error("600", "接收推免人数不能少于研究生院下发推免指标", false);
            case "4":
                return Result.error("600", "拟招生人数不等于推免生人数与公开招考人数之和",false);
            case "5":
                return Result.error("600", "招生专业人数表无数据，请先维护",false);
            default:
                return Result.success(true);
        }
    }
}
