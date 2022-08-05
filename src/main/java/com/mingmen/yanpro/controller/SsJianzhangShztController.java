package com.mingmen.yanpro.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsJianzhangShzt;
import com.mingmen.yanpro.service.*;
import com.mingmen.yanpro.utils.JianzhangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ss_jianzhang_shzt")
public class SsJianzhangShztController {

    @Autowired
    private SsjianzhangZykskmService ssjianzhangZykskmService;
    @Autowired
    private SsJianzhangZszyrsService ssJianzhangZszyrsService;
    @Autowired
    private SsjianzhangZydsService ssjianzhangZydsService;

    @Autowired
    private SsJianzhangShztService ssJianzhangShztService;

    @Resource
    private SsJianzhangZyxwggService ssJianzhangZyxwggService;

    @GetMapping
    public List<SsJianzhangShzt> index(){
        return ssJianzhangShztService.list();
    }

    @GetMapping("/getShzt")
    public Result getShzt(@RequestParam String YXSDM,
                          @RequestParam String NF){
        return Result.success(ssJianzhangShztService.getShzt(YXSDM, NF));
    }

    //研院用的审核状态查询，必须要用院系所名称，院系所代码不行
    @GetMapping("/getShztYy")
    public Result getShztYy(@RequestParam String YXSMC,
                            @RequestParam String NF){
        return Result.success(ssJianzhangShztService.getShztYy(YXSMC, NF));
    }


    @GetMapping("/nextYearShzt")
    public void nextYearShzt(@RequestParam String YXSDM){
        ssJianzhangShztService.nextYearShzt(YXSDM);
    }

    // 学院秘书用的审核状态更改接口
    // 前端设置，如果返回的是FALSE，则提示当前不在简章编制编辑时间内
    @GetMapping("/setShztXyms")
    public Result setShztXyms(@RequestParam String YXSDM, @RequestParam String NF){
        return Result.success(ssJianzhangShztService.setShztXyms(YXSDM, NF));
    }

    // 学院领导或研院用的审核状态更改接口
    @Transactional
    @GetMapping("/setShztLdOrYy")
    public Result setShztLdOrYy(@RequestParam String YXSDM,
                                 @RequestParam String NF,
                                 @RequestParam String SHZT,
                                 @RequestParam String YHZ,
                                 @RequestParam String SHENHEREN){
        //String onlyNF = NF.substring(11,15);
        // 先更新简章编制表格们的审核人和审核时间
        ssJianzhangZszyrsService.updateSHR_SHSJ(YXSDM, NF, SHENHEREN, YHZ);
        ssjianzhangZydsService.updateSHR_SHSJ(YXSDM, NF, SHENHEREN, YHZ);
        ssjianzhangZykskmService.updateSHR_SHSJ(YXSDM, NF, SHENHEREN, YHZ);
        ssJianzhangZyxwggService.updateSHR_SHSJ(YXSDM, NF, SHENHEREN, YHZ);
        // 再更简章编制提交控制表里的审核状态

        return Result.success(ssJianzhangShztService.setShztLdOrYy(YXSDM, NF, SHZT));
    }

    @PostMapping
    public int save(@RequestBody SsJianzhangShzt ssJianzhangShzt){
        return ssJianzhangShztService.saveSsJianzhangShzt(ssJianzhangShzt);
    }

    //分页查询 - mybatis-plus的方式
    @GetMapping("/page")
    public IPage<SsJianzhangShzt> findPage(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize,
                                             @RequestParam String YXSMC, // 院系所名称
                                             @RequestParam String NF){       // 年份
        IPage<SsJianzhangShzt> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SsJianzhangShzt> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("YXSMC", YXSMC);
        queryWrapper.like("NF",NF);
        queryWrapper.orderByDesc("ID");

        List<SsJianzhangShzt> ssJianzhangShzt_1 = ssJianzhangShztService.page(page, queryWrapper).getRecords();
        for (SsJianzhangShzt ssJianzhangShzt : ssJianzhangShzt_1) {
            ssJianzhangShzt.setShzt(JianzhangUtils.change(ssJianzhangShzt.getShzt()));
        }
        IPage<SsJianzhangShzt> iPage = ssJianzhangShztService.page(page, queryWrapper);
        iPage.setRecords(ssJianzhangShzt_1);

        return iPage;
    }
}
