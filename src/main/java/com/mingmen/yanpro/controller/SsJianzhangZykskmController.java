package com.mingmen.yanpro.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsJianzhangPinjie;
import com.mingmen.yanpro.dao.SsJianzhangZykskm;
import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.service.SsJianzhangZszyrsService;
import com.mingmen.yanpro.service.SsjianzhangZykskmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


import static com.mingmen.yanpro.utils.CustomMergeStrategy.writeExcel;

@RestController
@RequestMapping("/ss_jianzhang_zykskm")
public class SsJianzhangZykskmController {

    @Autowired
    private SsjianzhangZykskmService ssjianzhangZykskmService;



    // 根据院系所代码查询院系所名称，查的是基础表，可以复用
    @GetMapping("/yxsdm2yxsmc")
    public Result yxsdm2yxsmc(@RequestParam String YXSDM){
        return Result.success(ssjianzhangZykskmService.yxsdm2yxsmc(YXSDM));
    }
    // 查询当前材料是否允许提交，所有简章编制可以复用，查的是基础表
    @GetMapping("/getSFYXTJ")
    public Result getSFYXTJ(){
        return Result.success(ssjianzhangZykskmService.getSFYXTJ());
    }


    // 获取所有学院的名称，用于前端查询------这个是查询用的
    @GetMapping("/allCollege")
    public Result getAllCollege() {
        return Result.success(ssjianzhangZykskmService.getAllCollege());
    }

    // 选了学院之后，在后一个下拉框内显示专业，这个查的不是基础表，是从zykskm表里查出来的，等于是你没有插入，就没有这个专业------这个是查询用的
    // 根据院系所代码查询专业代码，mybatis-plus方式
    @GetMapping("/college2zy")
    public Result college2zy(@RequestParam String YXSDM) {

        List<String> zy = new ArrayList<>();
        QueryWrapper<SsJianzhangZykskm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", YXSDM);
        List<SsJianzhangZykskm> temp = ssjianzhangZykskmService.list(queryWrapper);
        for (SsJianzhangZykskm ssJianzhangZykskm : temp) {
            zy.add(ssJianzhangZykskm.getZymc());
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(zy);

        return Result.success( new ArrayList<>(hashSet));
    }

    // 选了学院和专业之后，在第三个下拉框内显示方向------这个是查询用的
    @GetMapping("/zy2fx")
    public Result zy2fx(@RequestParam String YXSDM, @RequestParam String ZYMC) {
        List<String> fx = new ArrayList<>();
        QueryWrapper<SsJianzhangZykskm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("YXSDM", YXSDM);
        queryWrapper.eq("ZYMC", ZYMC);
        List<SsJianzhangZykskm> temp = ssjianzhangZykskmService.list(queryWrapper);
        for (SsJianzhangZykskm ssJianzhangZykskm : temp) {
            fx.add(ssJianzhangZykskm.getYjfxmc());
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(fx);

        return Result.success( new ArrayList<>(hashSet));
    }

    // 根据院系所代码获取招生专业代码-----这个是新增简章编制用的
    @GetMapping("/getZYDM")
    public Result getZYDM(@RequestParam String YXSDM, @RequestParam String NF){
        return Result.success(ssjianzhangZykskmService.getZYDM(YXSDM, NF));
    }

    // 根据招生专业代码获取招生专业名称-----这个是新增简章编制用的，选择专业代码后，自动弹出专业名称
    @GetMapping("/getZYMC")
    public Result getZYMC(@RequestParam String ZYDM, @RequestParam String NF){
        return Result.success(ssjianzhangZykskmService.getZYMC(ZYDM, NF));
    }

    // 根据专业名称获取研究方向代码-----这个是新增简章编制用的，选择专业后，列出该专业下的所有方向
    @GetMapping("/getYJFXDM")
    public Result getYJFXDM(@RequestParam String ZYMC, @RequestParam String NF){
        return Result.success(ssjianzhangZykskmService.getYJFXDM(ZYMC, NF));
    }

    // 根据研究方向代码和专业名称获取研究方向名称-----这个是新增简章编制用的，选择研究方向代码后自动弹出研究方向名称
    @GetMapping("/getYJFXMC")
    public Result getYJFXMC(@RequestParam String YJFXDM, @RequestParam String ZYMC, @RequestParam String NF){
        return Result.success(ssjianzhangZykskmService.getYJFXMC(YJFXDM, ZYMC, NF));
    }

    // 获取所有的考试科目代码-----这个是新增简章编制用的，KSDY是考试单元
    @GetMapping("/getKSKMDM")
    public Result getKSKMDM(@RequestParam String KSDY){
        return Result.success(ssjianzhangZykskmService.getKSKMDM(KSDY));
    }

    // 获取所有的考试科目代码-----这个是新增简章编制用的，选择考试科目代码后，自动弹出考试科目名称
    @GetMapping("/getKSKMMC")
    public Result getKSKMMC(@RequestParam String KSKMDM){
        return Result.success(ssjianzhangZykskmService.getKMbyDM(KSKMDM));
    }

    // 获取所有信息
    @GetMapping
    public Result index(){
        return Result.success(ssjianzhangZykskmService.list());
    }

    // 根据id删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(ssjianzhangZykskmService.removeById(id));
    }

    // 批量删除 - mybatis-plus的方式
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(ssjianzhangZykskmService.removeByIds(ids));
    }


    // 保存或更新
    @PostMapping
    public Result save(@RequestBody SsJianzhangZykskm ssJianzhangZykskm){
        return Result.success(ssjianzhangZykskmService.saveSsjianzhangZykskm(ssJianzhangZykskm));
    }

    //分页查询 - mybatis-plus的方式
    @GetMapping("/page")
    public IPage<SsJianzhangZykskm> findPage(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize,
                                             @RequestParam String YXSDM, // 院系所代码
                                             @RequestParam String ZYMC,   // 专业名称
                                             @RequestParam String YJFXMC,     // 研究方向名称
                                             @RequestParam String NF){       // 年份
        IPage<SsJianzhangZykskm> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SsJianzhangZykskm> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("YXSDM", YXSDM);
        queryWrapper.like("ZYMC",ZYMC);
        queryWrapper.like("YJFXMC",YJFXMC);
        queryWrapper.like("NF",NF);
        queryWrapper.orderByDesc("ID");

        List<SsJianzhangZykskm> ssJianzhangZykskms = ssjianzhangZykskmService.page(page, queryWrapper).getRecords();
        for(SsJianzhangZykskm temp: ssJianzhangZykskms){
            temp.setZzllmc(ssjianzhangZykskmService.getKMbyDM(temp.getZzllm()));
            temp.setWgymc(ssjianzhangZykskmService.getKMbyDM(temp.getWgym()));
            temp.setYwk1mc(ssjianzhangZykskmService.getKMbyDM(temp.getYwk1m()));
            temp.setYwk2mc(ssjianzhangZykskmService.getKMbyDM(temp.getYwk2m()));
        }

        return ssjianzhangZykskmService.page(page, queryWrapper).setRecords(ssJianzhangZykskms);
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
        List<SsJianzhangZykskm> list = reader.readAll(SsJianzhangZykskm.class);
        inputStream.close();
        List<SsJianzhangZykskm> ssJianzhangZykskmList = new ArrayList<>();
        for(SsJianzhangZykskm temp : list){
            temp.setScr(userFileReq.getXm());
            temp.setYxsdm(userFileReq.getYxsdm());
            temp.setYxsmc(ssjianzhangZykskmService.yxsdm2yxsmc(userFileReq.getYxsdm()));
            temp.setNf(userFileReq.getNf());
            ssJianzhangZykskmList.add(temp);
        }
        int itemCount = list.size();
        List<SsJianzhangZykskm> failList = ssjianzhangZykskmService.saveWihtCheck(ssJianzhangZykskmList);
        if(failList.size()!=0){
            return Result.error("600","共上传" + itemCount + "条数据，其中" + StrUtil.toString(itemCount- failList.size()) +
                    "条数据上传成功，" + failList.size() + "条数据上传失败。失败数据已下载，请更正后重新提交。",failList);
        } else {
            return Result.success("200","成功上传" + itemCount + "条数据",true);
        }
    }

    ////////////////////以下接口是拼接简章编制用的////////////////////////////

    @GetMapping("/ss_jianzhang_pinjie")
    public Result jianZhangPinJie(@RequestParam String YXSDM, @RequestParam String NF){
        List<SsJianzhangPinjie> list = ssjianzhangZykskmService.jianZhangPinJie(YXSDM, NF);
        return Result.success(list);
    }

    @GetMapping("/ss_jianzhang_pinjie_download")
    public void pinJieDownload(@RequestParam String YXSDM, @RequestParam String NF, HttpServletResponse response) throws IOException {
        List<SsJianzhangPinjie> list = ssjianzhangZykskmService.jianZhangPinJie(YXSDM, NF);

        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(ssjianzhangZykskmService.yxsdm2yxsmc(YXSDM)+"招生简章.xlsx", "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(writeExcel(list));
        os.flush();
        os.close();
    }
}
