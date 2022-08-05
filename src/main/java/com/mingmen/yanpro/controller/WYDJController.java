package com.mingmen.yanpro.controller;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.TJLXDao;
import com.mingmen.yanpro.dao.WYDJDao;
import com.mingmen.yanpro.service.WYDJService;
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
@RequestMapping("/sys/data/tmInfo/WYDJ")
public class WYDJController {
    @Autowired
    private WYDJService wydjService;

    //查询全部操作
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(wydjService.selectAll());
    }

    //按照通过项目代码查找
    @GetMapping("/selectByTGXMDM/{TGXMDM}")
    public Result selectByTGXMDM(@PathVariable String TGXMDM) {
        return Result.success(wydjService.selectByTGXMDM(TGXMDM));
    }
    //按照通过项目名称查找
    @GetMapping("/selectByTGXMMC/{TGXMMC}")
    public Result selectByTGXMMC(@PathVariable String TGXMMC) {
        return Result.success(wydjService.selectByTGXMMC(TGXMMC));
    }

    //删除操作
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable Integer ID) {
        return Result.success(wydjService.delete(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(wydjService.deleteBatch(list));
    }


    //分页查询
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String mc) {
        return Result.success(wydjService.findPage(pageNum, pageSize, mc));
    }

    //新增和修改，查询通过项目代码，存在则更新，不存在则插入
    @PostMapping("/save")
    public Result save(@RequestBody WYDJDao wydjDao){
        return Result.success(wydjService.save(wydjDao));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
//      数据库所有数据
        List<WYDJDao> list = wydjService.selectAll();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.addHeaderAlias("ID","ID");
        writer.addHeaderAlias("TGXMDM","通过项目代码");
        writer.addHeaderAlias("TGXMMC","通过项目名称");

        writer.write(list,true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("外语等级","UTF-8");
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
        List<WYDJDao> wydjDaos = CollUtil.newArrayList();
        for(List<Object> row : list){
            WYDJDao wydjDao = new WYDJDao();
            wydjDao.setTGXMDM(row.get(0).toString());
            wydjDao.setTGXMMC(row.get(1).toString());
            wydjDaos.add(wydjDao);
        }
        wydjService.saveBatch(wydjDaos);
        return true;
    }
}
