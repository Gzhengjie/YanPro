package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.service.YXSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/YXS")
public class YXSController {

    @Autowired
    private YXSService yxsService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody YXSDao yxsDao){
        //新增或者更新
        Result result = new Result();
        return result.success(yxsService.save(yxsDao));
    }

    //查询所有
    @GetMapping("/selectAll")
    public Result selectAll(){
        Result result = new Result();
        return result.success(yxsService.selectAll());
    }

    //按院系所名称查询
    @GetMapping("/selectByMC")
    public Result selectByMC(@RequestParam String YXSMC) {
        Result result = new Result();
        return result.success(yxsService.selectByMC(YXSMC));
    }

    //删除
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable String ID){
        Result result = new Result();
        return result.success(yxsService.deleteByID(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<String> IDs) {
        Result result = new Result();
        return result.success(yxsService.deleteBatch(IDs));
    }

    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查出所有数据
        List<YXSDao> list = yxsService.selectAll();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("YXSDM","院系所代码");
        writer.addHeaderAlias("YXSMC","院系所名称");
        writer.addHeaderAlias("BZ","备注");

        //一次性写出list内对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("院系所基础表","UTF-8");
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
        List<YXSDao> YXSDaos = CollUtil.newArrayList();
        for (List<Object> row : list){
            YXSDao yxsDao = new YXSDao();
            yxsDao.setYXSDM(row.get(0).toString());
            yxsDao.setYXSMC(row.get(1).toString());
            yxsDao.setBZ(row.get(2).toString());
            YXSDaos.add(yxsDao);
        }
        yxsService.saveBatch(YXSDaos);
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
        List<YXSDao> data = yxsService.selectPage(pageNum,pageSize);
        Integer total = yxsService.selectTotal();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        Result result = new Result();
        return result.success(res);
    }
}
