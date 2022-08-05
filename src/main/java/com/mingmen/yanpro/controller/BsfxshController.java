package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.BszszyfxDao;
import com.mingmen.yanpro.dao.BszzbfpDao;
import com.mingmen.yanpro.service.BszszyfxService;
import com.mingmen.yanpro.service.BszzbfpService;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/BSFXSH")
public class BsfxshController {

    @Autowired
    private BszszyfxService bszszyfxService;

    /**
     * 单个审核
     * @param ID
     * @param SHZT
     * @return
     */
    @RequestMapping("/SH")  //单个审核
    public Result xxsh(@RequestParam Integer ID,
                       @RequestParam Integer SHZT) {  //输入ID、审核状态

        BszszyfxDao bszszyfxDao = new BszszyfxDao();
        bszszyfxDao.setID(ID);
        bszszyfxDao.setSHZT(SHZT);
        if (SHZT == 1) {
            bszszyfxDao.setSCR(TokenUtils.getCurrentUser().getXM());
            bszszyfxDao.setSCSJ(DateUtil.now());
        }
        else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            bszszyfxDao.setYYSHR(TokenUtils.getCurrentUser().getXM());
            bszszyfxDao.setYYSHSJ(DateUtil.now());
        } else {
            bszszyfxDao.setXYSHR(TokenUtils.getCurrentUser().getXM());
            bszszyfxDao.setXYSHSJ(DateUtil.now());
        }
        return Result.success(bszszyfxService.save(bszszyfxDao));

    }

    /**
     * 批量审核
     * @param IDs
     * @param SHZT
     * @return
     */
    @PostMapping("/SH/batch")
    public Result xxbatchsh(@RequestParam List<Integer> IDs, @RequestParam Integer SHZT) {
        for(Integer ID : IDs){
            BszszyfxDao bszszyfxDao = new BszszyfxDao();
            bszszyfxDao.setID(ID);
            bszszyfxDao.setSHZT(SHZT);
            if (SHZT == 1) {
                bszszyfxDao.setSCR(TokenUtils.getCurrentUser().getXM());
                bszszyfxDao.setSCSJ(DateUtil.now());
            }
            else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
                bszszyfxDao.setYYSHR(TokenUtils.getCurrentUser().getXM());
                bszszyfxDao.setYYSHSJ(DateUtil.now());
            } else {
                bszszyfxDao.setXYSHR(TokenUtils.getCurrentUser().getXM());
                bszszyfxDao.setXYSHSJ(DateUtil.now());
            }
            bszszyfxService.save(bszszyfxDao);
        }
        return Result.success(true);
    }
//    //新增和修改
//    @PostMapping("/save")
//    public Result save(@RequestBody BszzbfpDao bszzbfpDao){
//        //新增或者更新
//        Result result = new Result();
//        return result.success(bszzbfpService.save(bszzbfpDao));
//    }
//
//    //查询所有
//    @GetMapping("/selectAll")
//    public Result selectAll(){
//        Result result = new Result();
//        return result.success(bszzbfpService.selectAll());
//    }
//
//    //按专业名称查询
//    @GetMapping("/selectByMC")
//    public Result selectByMC(@RequestParam String YXSMC) {
//        Result result = new Result();
//        return result.success(bszzbfpService.selectByMC(YXSMC));
//    }
//
//    //删除
//    @DeleteMapping("/delete/{ID}")
//    public Result delete(@PathVariable String ID){
//        Result result = new Result();
//        return result.success(bszzbfpService.deleteByID(ID));
//    }
//
//    //批量删除
//    @PostMapping("/deleteBatch")
//    public Result deleteBatch(@RequestBody List<String> IDs) {
//        Result result = new Result();
//        return result.success(bszzbfpService.deleteBatch(IDs));
//    }
//
//    //导出接口
//    @GetMapping("/export")
//    public void export(HttpServletResponse response) throws Exception{
//        //从数据库查出所有数据
//        List<BszzbfpDao> list = bszzbfpService.selectAll();
//        //在内存操作，写出到浏览器
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//        //自定义标题别名
//        writer.addHeaderAlias("YXSDM","院系所代码");
//        writer.addHeaderAlias("YXSMC","院系所名称");
//        writer.addHeaderAlias("ZSSL","招生数量");
//        writer.addHeaderAlias("ZSZL","招生增量");
//        writer.addHeaderAlias("ZSLX","招生类型");
//        writer.addHeaderAlias("TMSL","推免数量");
//        writer.addHeaderAlias("TMZL","推免增量");
//        writer.addHeaderAlias("NF","年份");
//        writer.addHeaderAlias("SCR","上传人");
//        writer.addHeaderAlias("SCSJ","上传时间");
//        writer.addHeaderAlias("GXSJ","更新时间");
//
//        //一次性写出list内对象到Excel，使用默认样式，强制输出标题
//        writer.write(list,true);
//
//        //设置浏览器响应格式
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        String fileName = URLEncoder.encode("博士总指标分配表","UTF-8");
//        response.setHeader("Content-Disposition","attachment;filename="+ fileName + ".xlsx");
//
//        ServletOutputStream out = response.getOutputStream();
//        writer.flush(out,true);
//        out.close();
//        writer.close();
//    }
//
//    @PostMapping("/import")
//    public boolean imp(MultipartFile file) throws Exception{
//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        List<List<Object>> list = reader.read(1);
//        List<BszzbfpDao> BszzbfpDaos = CollUtil.newArrayList();
//        for (List<Object> row : list){
//            BszzbfpDao bszzbfpDao = new BszzbfpDao();
//            bszzbfpDao.setYXSDM(row.get(0).toString());
//            bszzbfpDao.setYXSMC(row.get(1).toString());
//            bszzbfpDao.setZSSL(row.get(2).toString());
//            bszzbfpDao.setZSZL(row.get(3).toString());
//            bszzbfpDao.setTMSL(row.get(4).toString());
//            bszzbfpDao.setTMZL(row.get(5).toString());
//            bszzbfpDao.setNF(row.get(6).toString());
//            bszzbfpDao.setSCR(row.get(7).toString());
////            bszzbfpDao.setSCSJ(row.get(8).toString());
////            bszzbfpDao.setGXSJ(row.get(9).toString());
//            BszzbfpDaos.add(bszzbfpDao);
//        }
//        bszzbfpService.saveBatch(BszzbfpDaos);
//        return true;
//    }
//    /**
//     * 分页查询
//     * 接口路径：/user/page?pageNum=1&pageSize=5
//     * @RequestParam 接收参数
//     * limit 第一个参数 = （pageNum-1）*pageSize
//     */
//    @GetMapping("/findPage")
//    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
//        pageNum = (pageNum - 1) * pageSize;
//        List<BszzbfpDao> data = bszzbfpService.selectPage(pageNum,pageSize);
//        Integer total = bszzbfpService.selectTotal();
//        Map<String,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        Result result = new Result();
//        return result.success(res);
//    }



}
