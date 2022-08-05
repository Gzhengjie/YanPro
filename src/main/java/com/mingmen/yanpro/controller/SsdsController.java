package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsdsDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.service.SsdsService;
import com.mingmen.yanpro.service.YXSService;
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
@RequestMapping("/SSDS")
public class SsdsController {

    @Autowired
    private SsdsService ssdsService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody SsdsDao ssdsDao){
        //新增或者更新
        Result result = new Result();
        return result.success(ssdsService.save(ssdsDao));
    }

    //查询所有
    @GetMapping("/selectAll")
    public Result selectAll(){
        Result result = new Result();
        return result.success(ssdsService.selectAll());
    }

    //按院系所名称查询
    @GetMapping("/selectByMC")
    public Result selectByMC(@RequestParam String DSXM) {
        Result result = new Result();
        return result.success(ssdsService.selectByMC(DSXM));
    }

    //删除
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable String ID){
        Result result = new Result();
        return result.success(ssdsService.deleteByID(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<String> IDs) {
        Result result = new Result();
        return result.success(ssdsService.deleteBatch(IDs));
    }

    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查出所有数据
        List<SsdsDao> list = ssdsService.selectAll();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("ZGH","职工号");
        writer.addHeaderAlias("DSXM","导师姓名");
        writer.addHeaderAlias("SZYXDM","所在院系所代码");
        writer.addHeaderAlias("SZYXMC","所在院系所名称");
        writer.addHeaderAlias("ZC","职称");
        writer.addHeaderAlias("XSZSYXDM","学术型硕士招生院系所代码");
        writer.addHeaderAlias("XSZSYXMC","学术型硕士招生院系所名称");
        writer.addHeaderAlias("XSZSZYDM","学术型硕士招生专业代码");
        writer.addHeaderAlias("XSZSZYMC","学术型硕士招生专业名称");
        writer.addHeaderAlias("ZSZSYXDM","专业学位硕士招生院系所代码");
        writer.addHeaderAlias("ZSZSYXMC","专业学位硕士招生院系所名称");
        writer.addHeaderAlias("ZSZSZYDM","专业学位硕士招生专业代码");
        writer.addHeaderAlias("ZSZSZYMC","专业学位硕士招生专业代码");
        writer.addHeaderAlias("BZ","备注");
        writer.addHeaderAlias("NF","年份");

        //一次性写出list内对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("硕士招生导师表","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+ fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }

    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<List<Object>> list = reader.read(1);
        List<SsdsDao> ssdsDaos = CollUtil.newArrayList();
        for (List<Object> row : list){
            SsdsDao ssdsDao = new SsdsDao();
            ssdsDao.setZGH(row.get(0).toString());
            ssdsDao.setDSXM(row.get(1).toString());
            ssdsDao.setSZYXDM(row.get(2).toString());
            ssdsDao.setSZYXMC(row.get(3).toString());
            ssdsDao.setZC(row.get(4).toString());
            ssdsDao.setXSZSYXDM(row.get(5).toString());
            ssdsDao.setXSZSYXMC(row.get(6).toString());
            ssdsDao.setXSZSZYDM(row.get(7).toString());
            ssdsDao.setXSZSZYMC(row.get(8).toString());
            ssdsDao.setZSZSYXDM(row.get(9).toString());
            ssdsDao.setZSZSYXMC(row.get(10).toString());
            ssdsDao.setZSZSZYDM(row.get(11).toString());
            ssdsDao.setZSZSZYMC(row.get(12).toString());
            ssdsDao.setBZ(row.get(13).toString());
            ssdsDao.setNF(row.get(14).toString());
            ssdsDaos.add(ssdsDao);
        }
        ssdsService.saveBatch(ssdsDaos);
        return true;
    }
    /**
     * 分页查询
     * 接口路径：/user/page?pageNum=1&pageSize=5
     * @RequestParam 接收参数
     * limit 第一个参数 = （pageNum-1）*pageSize
     */
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize
                                       ){
        pageNum = (pageNum - 1) * pageSize;
        List<SsdsDao> data = ssdsService.selectPage(pageNum,pageSize);
        Integer total = ssdsService.selectTotal();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        Result result = new Result();
        return result.success(res);
    }
}
