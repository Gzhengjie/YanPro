package com.mingmen.yanpro.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.BsjianzhangshztDao;
import com.mingmen.yanpro.service.BsjianzhangshztService;
import com.mingmen.yanpro.service.BszsjzService;
import com.mingmen.yanpro.service.BszyzsrsService;
import com.mingmen.yanpro.utils.JianzhangUtils;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bsjianzhangshzt")
public class BsjianzhangshztController {

    @Autowired
    private BsjianzhangshztService bsjianzhangshztService;

    @Autowired
    private BszsjzService bszsjzService;

    @Autowired
    private BszyzsrsService bszyzsrsService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody BsjianzhangshztDao bsjianzhangshztDao) {
        //新增或更新
        return Result.success(bsjianzhangshztService.saveBsshzt(bsjianzhangshztDao));
    }

    //查询全部操作
    @GetMapping("/selectAll")
    public Result index() {
        return Result.success(bsjianzhangshztService.selectAll());
    }

    //查询当前材料的提交状态
    @GetMapping("/getSHZT")
    public Result getSHZT(@RequestParam String nf) {
        Result result = new Result();
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        Integer shzt = bsjianzhangshztService.getSHZT(yxsdm,nf);
        if (shzt==6){
            result.setCode("401");
            result.setMsg("当前年份暂无数据");
            return result;
        }
        else{
            return Result.success(shzt);
        }
    }

    //返回审核状态表中年份最小值
    @GetMapping("/minyear")
    public Result minYear() {
        String YXSDM = TokenUtils.getCurrentUser().getYXSDM();
        return Result.success(bsjianzhangshztService.minYear(YXSDM));
    }

    @GetMapping("/alljzminyear")
    public Result allJZminyear() {
        return Result.success(bsjianzhangshztService.allJZminyear());
    }


    //学院秘书用的审核状态更改接口
    @GetMapping("/setXYMSshzt")
    public Result setXYMSshzt(@RequestParam String nf) {
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        if (bsjianzhangshztService.setXYMSshzt(yxsdm, nf)==true){
            return Result.success(true);
        }
        else{
            return Result.error("401","提交数据不可为空");
        }
    }

    //学院领导和研究生院用的审核状态更改接口
    @GetMapping ("/setXYLDYYshzt")
    public Result setXYLDYYshzt(@RequestParam String yxsdm,
                                @RequestParam String nf,
                                @RequestParam String shzt){
        String shr = TokenUtils.getCurrentUser().getXM();
        String yhz = TokenUtils.getCurrentUser().getYHZ();
        //先更新学院专业人数表和博士简章表的审核人和审核时间
        bszyzsrsService.updateShzt(yxsdm, nf, yhz, shr);
        bszsjzService.updateShzt(yxsdm, nf, yhz, shr);
        //再更新博士简章编制提交控制表里的审核状态
        return Result.success(bsjianzhangshztService.setXYLDYYshzt(yxsdm, nf, shzt));
    }

    //分页查询
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String yxsmc,
                           @RequestParam(defaultValue = "") String nf){
        IPage<BsjianzhangshztDao> page = bsjianzhangshztService.findPage(new Page<>(pageNum, pageSize), yxsmc, nf);
        List<BsjianzhangshztDao> list = page.getRecords();
        for(BsjianzhangshztDao bsjianzhangshztDao:list) {
            bsjianzhangshztDao.setCode(bsjianzhangshztDao.getSHZT());
            bsjianzhangshztDao.setSHZT(JianzhangUtils.change(bsjianzhangshztDao.getSHZT()));
        }

        page.setRecords(list);
        return Result.success(page);
//        return Result.success(bsjianzhangshztService.findPage(new Page<>(pageNum, pageSize), yxsmc, nf));
    }

}
