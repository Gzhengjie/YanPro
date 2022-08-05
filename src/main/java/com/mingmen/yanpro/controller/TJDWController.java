package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.TJDWDao;
import com.mingmen.yanpro.dao.TJDWDao;
import com.mingmen.yanpro.service.TJDWService;
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
@RequestMapping("/sys/data/tmInfo/TJDW")
public class TJDWController {
    @Autowired
    private TJDWService tjdwService;

    //查询全部操作
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(tjdwService.selectAll());
    }

    //按照推荐单位代码查找
    @GetMapping("/selectByTJDWDM/{TJDWDM}")
    public Result selectByTJDWDM(@PathVariable String TJDWDM) {
        return Result.success(tjdwService.selectByTJDWDM(TJDWDM));
    }

    //按照推荐单位名称查找
    @GetMapping("/selectByTJDWMC/{TJDWMC}")
    public Result selectByTJDWMC(@PathVariable String TJDWMC) {
        return Result.success(tjdwService.selectByTJDWMC(TJDWMC));
    }

    //删除操作
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable Integer ID) {
        return Result.success(tjdwService.delete(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(tjdwService.deleteBatch(list));
    }

    //分页查询
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestParam(defaultValue = "") String mc) {
        return Result.success(tjdwService.findPage(pageNum, pageSize, mc));
    }

    //新增和修改，查询接收类别，存在则更新，不存在则插入
    @PostMapping("/save")
    public Result save(@RequestBody TJDWDao tjdwDao){return Result.success(tjdwService.save(tjdwDao));}

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
//      数据库所有数据
        List<TJDWDao> list = tjdwService.selectAll();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.addHeaderAlias("TJDWDM","推荐单位代码");
        writer.addHeaderAlias("TJDWMC","推荐单位名称");

        writer.setOnlyAlias(true);
        writer.write(list,true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("推荐单位","UTF-8");
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
        List<TJDWDao> tjdwDaos = CollUtil.newArrayList();
        for(List<Object> row : list){
            TJDWDao tjdwDao = new TJDWDao();
            tjdwDao.setTJDWDM(row.get(0).toString());
            tjdwDao.setTJDWMC(row.get(1).toString());
            tjdwDaos.add(tjdwDao);
        }
        tjdwService.saveBatch(tjdwDaos);
        return true;
    }

}
