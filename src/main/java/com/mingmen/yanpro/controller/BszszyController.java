package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.service.BszszyService;
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
@RequestMapping("/BSZSZY")
public class BszszyController {

    @Autowired
    private BszszyService bszszyService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody BszszyDao bszszyDao){
        //新增或者更新
        Result result = new Result();
        return result.success(bszszyService.save(bszszyDao));
    }

    //查询所有
    @GetMapping("/selectAll")
    public Result selectAll(){
        Result result = new Result();
        return result.success(bszszyService.selectAll());
    }

    //按专业名称查询
    @GetMapping("/selectByMC")
    public Result selectByMC(@RequestParam String ZYMC) {
        Result result = new Result();
        return result.success(bszszyService.selectByMC(ZYMC));
    }

    //删除
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable String ID){
        Result result = new Result();
        return result.success(bszszyService.deleteByID(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<String> IDs) {
        Result result = new Result();
        return result.success(bszszyService.deleteBatch(IDs));
    }

    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查出所有数据
        List<BszszyDao> list = bszszyService.selectAll();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("YXSDM","院系所代码");
        writer.addHeaderAlias("ZYDM","专业代码");
        writer.addHeaderAlias("ZYMC","专业名称");
        writer.addHeaderAlias("BZ","备注");
        writer.addHeaderAlias("NF","年份");

        //一次性写出list内对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("博士招生专业表","UTF-8");
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
        List<BszszyDao> BszszyDaos = CollUtil.newArrayList();
        for (List<Object> row : list){
            BszszyDao bszszyDao = new BszszyDao();
            bszszyDao.setYXSDM(row.get(0).toString());
            bszszyDao.setZYDM(row.get(1).toString());
            bszszyDao.setZYMC(row.get(2).toString());
            bszszyDao.setBZ(row.get(3).toString());
            bszszyDao.setNF(row.get(4).toString());
            BszszyDaos.add(bszszyDao);
        }
        bszszyService.saveBatch(BszszyDaos);
        return true;
    }
    /**
     * 分页查询
     * 接口路径：/user/page?pageNum=1&pageSize=5
     * @RequestParam 接收参数
     * limit 第一个参数 = （pageNum-1）*pageSize
     */
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        pageNum = (pageNum - 1) * pageSize;
        List<BszszyDao> data = bszszyService.selectPage(pageNum,pageSize);
        Integer total = bszszyService.selectTotal();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        Result result = new Result();
        return result.success(res);
    }
}
