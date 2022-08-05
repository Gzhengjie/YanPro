package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.PMFSDao;
import com.mingmen.yanpro.service.PMFSService;
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
@RequestMapping("/sys/data/tmInfo/PMFS")
public class PMFSController {
    @Autowired
    private PMFSService pmfsService;

    //查询全部操作
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(pmfsService.selectAll());
    }

    //按照排名方式代码查找
    @GetMapping("/selectByPMFSDM/{PMFSDM}")
    public Result selectByByPMFSDM(@PathVariable String PMFSDM) {
        return Result.success(pmfsService.selectByPMFSDM(PMFSDM));
    }

    //按照排名方式名称代码查找
    @GetMapping("/selectByPMFSMC/{PMFSMC}")
    public Result selectByByPMFSMC(@PathVariable String PMFSMC) {
        return Result.success(pmfsService.selectByPMFSMC(PMFSMC));
    }

    //删除操作
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable Integer ID) {
        return Result.success(pmfsService.delete(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> list) {
        return Result.success(pmfsService.deleteBatch(list));
    }

    //分页查询
    @GetMapping("/findPage")
    public  Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestParam(defaultValue = "") String mc) {
        return Result.success(pmfsService.findPage(pageNum, pageSize,mc));
    }
    //新增和修改，查询排名方式代码,存在则更新，不存在则插入
    @PostMapping("/save")
    public Result save(@RequestBody PMFSDao pmfsDao){
        return Result.success(pmfsService.save(pmfsDao));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
//      数据库所有数据
        List<PMFSDao> list = pmfsService.selectAll();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.addHeaderAlias("ID","ID");
        writer.addHeaderAlias("PMFSDM","排名方式代码");
        writer.addHeaderAlias("PMFSMC","排名方式名称");
        writer.write(list,true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("排名方式","UTF-8");
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
        List<PMFSDao> pmfsDaos = CollUtil.newArrayList();
        for(List<Object> row : list){
            PMFSDao pmfsDao = new PMFSDao();
            pmfsDao.setPMFSDM(row.get(0).toString());
            pmfsDao.setPMFSMC(row.get(1).toString());
            pmfsDaos.add(pmfsDao);
        }
        pmfsService.saveBatch(pmfsDaos);
        return true;


    }

}
