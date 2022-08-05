package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.TJDWDao;
import com.mingmen.yanpro.dao.TJLXDao;
import com.mingmen.yanpro.service.TJLXService;
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
@RequestMapping("/sys/data/tmInfo/TJLX")
public class TJLXController {

    @Autowired
    private TJLXService tjlxService;

    //查询全部操作
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(tjlxService.selectAll());
    }

    //按照推荐类型码查找
    @GetMapping("/selectByTJLXM/{TJLXM}")
    public Result selectByTJLXM(@PathVariable String TJLXM) {

        return Result.success(tjlxService.selectByTJLXM(TJLXM));
    }

    //按照推荐类型名称查找
    @GetMapping("/selectByTJLXMC/{TJLXMC}")
    public Result selectByTJLXMC(@PathVariable String TJLXMC) {

        return Result.success(tjlxService.selectByTJLXMC(TJLXMC));
    }

    //删除操作
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable Integer ID) {
        return Result.success(tjlxService.delete(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(tjlxService.deleteBatch(list));
    }

    //分页查询
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String mc) {
        return Result.success(tjlxService.findPage(pageNum, pageSize, mc));
    }

    //新增和修改，查询推荐类型码，推荐类型码存在则更新，荐类型码不存在则插入
    @PostMapping("/save")
    public Result save(@RequestBody TJLXDao tjlxDao){
        return Result.success(tjlxService.save(tjlxDao));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
//      数据库所有数据
        List<TJLXDao> list = tjlxService.selectAll();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.addHeaderAlias("ID","ID");
        writer.addHeaderAlias("TJLXM","推荐类型代码");
        writer.addHeaderAlias("TJLXMC","推荐类型名称");
        writer.addHeaderAlias("BZ","备注");
        writer.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("推荐类型","UTF-8");
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
        List<TJLXDao> tjlxDaos = CollUtil.newArrayList();
        for(List<Object> row : list){
            TJLXDao tjlxDao = new TJLXDao();
            tjlxDao.setTJLXM(row.get(0).toString());
            tjlxDao.setTJLXMC(row.get(1).toString());
            tjlxDao.setBZ(row.get(2).toString());
            tjlxDaos.add(tjlxDao);
        }
        tjlxService.saveBatch(tjlxDaos);
        return true;
    }

}
