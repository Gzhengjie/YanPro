package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.BszszyDao;
import com.mingmen.yanpro.dao.BszszyfxDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.service.BszszyService;
import com.mingmen.yanpro.service.BszszyfxService;
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
@RequestMapping("/BSZSZYFX")
public class BszszyfxController {

    @Autowired
    private BszszyfxService bszszyfxService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody BszszyfxDao bszszyfxDao){
        //新增或者更新
        Result result = new Result();
        return result.success(bszszyfxService.save(bszszyfxDao));
    }

    //查询所有
    @GetMapping("/selectAll")
    public Result selectAll(){
        Result result = new Result();
        return result.success(bszszyfxService.selectAll());
    }

    //按院系所名称查询
    @GetMapping("/selectByMC")
    public Result selectByMC(@RequestParam String YJFXMC) {
        Result result = new Result();
        return result.success(bszszyfxService.selectByMC(YJFXMC));
    }

    //删除
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable String ID){
        Result result = new Result();
        return result.success(bszszyfxService.deleteByID(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<String> IDs) {
        Result result = new Result();
        return result.success(bszszyfxService.deleteBatch(IDs));
    }

    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查出所有数据
        List<BszszyfxDao> list = bszszyfxService.selectAll();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("ZYDM","专业代码");
        writer.addHeaderAlias("YJFXDM","研究方向代码");
        writer.addHeaderAlias("YJFXMC","研究方向名称");
        writer.addHeaderAlias("XXFS","学习方式");
        writer.addHeaderAlias("ZSLX","招生类型");
        writer.addHeaderAlias("BZ","备注");
        writer.addHeaderAlias("NF","年份");

        //一次性写出list内对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("博士招生专业方向表","UTF-8");
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
        List<BszszyfxDao> bszszyfxDaos = CollUtil.newArrayList();
        for (List<Object> row : list){
            BszszyfxDao bszszyfxDao = new BszszyfxDao();
            bszszyfxDao.setZYDM(row.get(0).toString());
            bszszyfxDao.setYJFXDM(row.get(1).toString());
            bszszyfxDao.setYJFXMC(row.get(2).toString());
            bszszyfxDao.setXXFS(row.get(3).toString());
            bszszyfxDao.setZSLX(row.get(4).toString());
            bszszyfxDao.setBZ(row.get(5).toString());
            bszszyfxDao.setNF(row.get(6).toString());
            bszszyfxDaos.add(bszszyfxDao);
        }
        bszszyfxService.saveBatch(bszszyfxDaos);
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
        List<BszszyfxDao> data = bszszyfxService.selectPage(pageNum,pageSize);
        Integer total = bszszyfxService.selectTotal();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        Result result = new Result();
        return result.success(res);
    }
}
