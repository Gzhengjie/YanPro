package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.JSLBDao;

import com.mingmen.yanpro.service.JSLBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/data/tmInfo/JSLB")
public class JSLBController {
    @Autowired
    private JSLBService jslbService;

    //查询全部操作
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(jslbService.selectAll());
    }



    //按照接收类别查找
    @GetMapping("/selectByJSLBDM/{JSLBDM}")
    public Result selectByJSLBDM(@PathVariable String JSLBDM) {
        return Result.success(jslbService.selectByJSLBDM(JSLBDM));
    }

    //按照接收类别名称查找
    @GetMapping("/selectByJSLBMC/{JSLBMC}")
    public Result selectByJSLBMC(@PathVariable String JSLBMC) {
        return Result.success(jslbService.selectByJSLBMC(JSLBMC));
    }




    //删除操作
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable Integer ID) {
        return Result.success(jslbService.delete(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(jslbService.deleteBatch(list));
    }





    //分页查询
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String mc) {
        return Result.success(jslbService.findPage(pageNum, pageSize, mc));
    }

    //新增和修改，查询接收类别，存在则更新，不存在则插入
    @PostMapping("/save")
    public Result save(@RequestBody JSLBDao jslbDao){
        return Result.success(jslbService.save(jslbDao));
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
//      数据库所有数据
        List<JSLBDao> list = jslbService.selectAll();
        ExcelWriter writer = ExcelUtil.getWriter(true);

//        writer.addHeaderAlias("ID","ID");
        writer.addHeaderAlias("JSLBDM","接收类型代码");
        writer.addHeaderAlias("JSLBMC","接收类型名称");

        writer.setOnlyAlias(true);
        writer.write(list,true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("接收类型","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();

    }

    @PostMapping("/import")
    public Boolean imp(MultipartFile file)throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<List<Object>> list = reader.read(1);
        List<JSLBDao> jslbDaos = CollUtil.newArrayList();
        for(List<Object> row : list){
            JSLBDao jslbDao = new JSLBDao();
            jslbDao.setJSLBDM(row.get(0).toString());
            jslbDao.setJSLBMC(row.get(1).toString());
              jslbDaos.add(jslbDao);
        }
        jslbService.saveBatch(jslbDaos);
        return true;
    }
}
