package com.mingmen.yanpro.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsJianzhangZyds;
import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.service.SsjianzhangZydsService;
import com.mingmen.yanpro.service.SsjianzhangZykskmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static com.mingmen.yanpro.utils.JianzhangUtils.str2list;

@RestController
@RequestMapping("/ss_jianzhang_zyds")
public class SsJianzhangZydsController {

    @Autowired
    private SsjianzhangZykskmService ssjianzhangZykskmService;

    @Autowired
    private SsjianzhangZydsService ssjianzhangZydsService;

    @GetMapping
    public Result index(){
        return Result.success(ssjianzhangZydsService.list());
    }

    // 根据id删除单条数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(ssjianzhangZydsService.removeById(id));
    }

    // 批量删除 - mybatis-plus的方式
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(ssjianzhangZydsService.removeByIds(ids));
    }

    // 获取所有学院的名称，用于前端查询
    @GetMapping("/allCollege")
    public Result getAllCollege() {
        return Result.success(ssjianzhangZykskmService.getAllCollege());
    }

    // 选了学院之后，在后一个下拉框内显示专业，这个查的不是基础表，是从zyds表里查出来的，等于是你没有插入，就没有这个专业
    @GetMapping("/college2zy")
    public Result college2zy(@RequestParam String YXSDM) {
        List<String> zy = new ArrayList<>();
        QueryWrapper<SsJianzhangZyds> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", YXSDM);
        List<SsJianzhangZyds> temp = ssjianzhangZydsService.list(queryWrapper);
        for (SsJianzhangZyds ssJianzhangZyds : temp) {
            zy.add(ssJianzhangZyds.getZymc());
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(zy);

        return Result.success(new ArrayList<>(hashSet));
    }

    // 选了学院和专业之后，在第三个下拉框内显示方向，这个查的不是基础表，是从zyds表里查出来的，等于是你没有插入，就没有这个专业
    @GetMapping("/zy2fx")
    public Result zy2fx(@RequestParam String YXSDM, @RequestParam String ZYMC) {
        List<String> zy = new ArrayList<>();
        QueryWrapper<SsJianzhangZyds> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", YXSDM);
        queryWrapper.eq("ZYMC", ZYMC);
        List<SsJianzhangZyds> temp = ssjianzhangZydsService.list(queryWrapper);
        for (SsJianzhangZyds ssJianzhangZyds : temp) {
            zy.add(ssJianzhangZyds.getYjfxmc());
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(zy);

        return Result.success(new ArrayList<>(hashSet));
    }

    // 根据学院代码和招生专业代码，获取导师姓名，这个是简章编制用的
    @GetMapping("/getDS")
    public Result getDS(@RequestParam String ZSYXSDM, @RequestParam String ZSZYDM, @RequestParam String NF){
        return Result.success(ssjianzhangZydsService.getDS(ZSYXSDM, ZSZYDM, NF));
    }


    // 插入或更新
    @PostMapping
    public Result save(@RequestBody SsJianzhangZyds ssJianzhangZyds){
        return Result.success(ssjianzhangZydsService.saveSsJianzhangZyds(ssJianzhangZyds));
    }


    //分页查询 - mybatis-plus的方式
    @GetMapping("/page")
    public IPage<SsJianzhangZyds> findPage(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize,
                                             @RequestParam String YXSDM, // 院系所代码
                                             @RequestParam String ZYMC,   // 专业名称
                                             @RequestParam String YJFXMC,     // 研究方向名称
                                             @RequestParam String NF){       // 年份
        IPage<SsJianzhangZyds> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SsJianzhangZyds> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("YXSDM", YXSDM);
        queryWrapper.like("ZYMC",ZYMC);
        queryWrapper.like("YJFXMC",YJFXMC);
        queryWrapper.like("NF",NF);
        queryWrapper.orderByDesc("ID");
        // 数据库里是string，前端要求是list，所以要转化一下才可以
        List<SsJianzhangZyds> ssJianzhangZydsList = new ArrayList<>();
        for(SsJianzhangZyds ssJianzhangZyds : ssjianzhangZydsService.page(page, queryWrapper).getRecords()){
            ssJianzhangZyds.setZdjslist(str2list(ssJianzhangZyds.getZdjs()));
            ssJianzhangZydsList.add(ssJianzhangZyds);
        }
        return ssjianzhangZydsService.page(page, queryWrapper).setRecords(ssJianzhangZydsList);
    }


    /**
     * excel 导入
     * @param userFileReq 自定义实体类
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(UserFileReq userFileReq) throws Exception{

        InputStream inputStream = userFileReq.getFile().getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<SsJianzhangZyds> list = reader.readAll(SsJianzhangZyds.class);
        inputStream.close();

        List<SsJianzhangZyds> ssJianzhangZydsList = new ArrayList<>();
        for(SsJianzhangZyds temp : list){
            temp.setScr(userFileReq.getXm());
            temp.setYxsdm(userFileReq.getYxsdm());
            temp.setYxsmc(ssjianzhangZykskmService.yxsdm2yxsmc(userFileReq.getYxsdm()));
            temp.setNf(userFileReq.getNf());
            temp.setZdjs(temp.getZdjs().replaceAll(" ","").replaceAll(",", "，"));
            ssJianzhangZydsList.add(temp);
        }
        int itemCount = list.size();
        List<SsJianzhangZyds> failList = ssjianzhangZydsService.saveWihtCheck(ssJianzhangZydsList);
        if(failList.size()!=0){
            return Result.error("600","共上传" + itemCount + "条数据，其中" + StrUtil.toString(itemCount- failList.size()) +
                    "条数据上传成功，" + failList.size() + "条数据上传失败。失败数据已下载，请更正后重新提交。",failList);
        } else {
            return Result.success("200","成功上传" + itemCount + "条数据",true);
        }
    }
}
