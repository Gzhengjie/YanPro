package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.GjzxjhDao;
import com.mingmen.yanpro.service.GjzxjhService;
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
@RequestMapping("/sys/data/lqInfo/gjzxjh")
public class GjzxjhController {

    @Autowired
    private GjzxjhService gjzxjhService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody GjzxjhDao gjzxjhDao) {
        //新增或更新
        return Result.success(gjzxjhService.save(gjzxjhDao));
    }

    //查询全部操作
    @GetMapping("/selectAll")
    public Result index() {
        return Result.success(gjzxjhService.selectAll());
    }

    //通过专项计划代码进行查询
    @GetMapping("/selectByZXJHDM/{ZXJHDM}")
    public Result selectByZxjhdm(@PathVariable String ZXJHDM) {return Result.success(gjzxjhService.selectByZxjhdm(ZXJHDM));}

    //通过专项计划名称进行查询
    @GetMapping("/selectByZXJHMC/{ZXJHMC}")
    public Result selectByZxjhmc(@PathVariable String ZXJHMC) {
        return Result.success(gjzxjhService.selectByZxjhmc(ZXJHMC));
    }

    //删除操作,通过专项计划代码进行删除
//    @DeleteMapping("/delete/{ZXJHDM}")
//    public Result delete(@PathVariable String ZXJHDM) {
//        return Result.success(gjzxjhService.deleteByZXJHDM(ZXJHDM));
//    }

    //删除操作，按照id进行删除
    @DeleteMapping("/delete/{ID}")
    public Result deleteById(@PathVariable Integer ID) {
        return Result.success(gjzxjhService.removeById(ID));
    }


    //批量删除
//    @PostMapping("/deleteBatch")
//    public Result deleteBatch(@RequestBody List<String> ZXJHDMs) {
//        return Result.success(gjzxjhService.deleteBatch(ZXJHDMs));
//    }
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(gjzxjhService.deleteBatch(ids));
    }

    /**
     * 分页查询
     * 接口路径：/sys/data/lqInfo/gjzxjh/findPage?pageNum=1&pageSize=5
     * @RequestParam 接收参数
     * limit 第一个参数 = （pageNum-1）*pageSize
     */
    @GetMapping("/findPage")
//    public Result findPage(@RequestParam Integer pageNum,
//                           @RequestParam Integer pageSize){
//        pageNum = (pageNum - 1) * pageSize;
//        List<GjzxjhDao> data = gjzxjhService.selectPage(pageNum,pageSize);
//        Integer total = gjzxjhService.selectTotal();
//        Map<String,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        Result result = new Result();
//        return result.success(res);
//    }
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String ZXJHDM,
                           @RequestParam(defaultValue = "") String ZXJHMC){
        pageNum = (pageNum - 1) * pageSize;
        ZXJHDM = "%" + ZXJHDM + "%";
        ZXJHMC = "%" + ZXJHMC + "%";
        List<GjzxjhDao> data = gjzxjhService.selectPage(pageNum,pageSize,ZXJHDM,ZXJHMC);
        Integer total = gjzxjhService.selectTotal(ZXJHDM,ZXJHMC);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        Result result = new Result();
        return result.success(res);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<GjzxjhDao> list = gjzxjhService.selectAll();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(true);
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("ZXJHDM", "专项计划代码");
        writer.addHeaderAlias("ZXJHMC", "专项计划名称");
        writer.addHeaderAlias("BZ", "备注");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("国家专项计划表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式1：通过JavaBean的方式读取Excel内的对象，但是要求表头必须是英文，跟JavaBean的属性要对应起来
        //List<GjzxjhDao> list = reader.readAll(GjzxjhDao.class);
        //方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<GjzxjhDao> gjzxjhDaos = CollUtil.newArrayList();
        for(List<Object> row:list) {
            GjzxjhDao gjzxjhDao = new GjzxjhDao();
            gjzxjhDao.setZXJHDM(row.get(0).toString());
            gjzxjhDao.setZXJHMC(row.get(1).toString());
            gjzxjhDao.setBZ(row.get(2).toString());
            gjzxjhDaos.add(gjzxjhDao);
        }

        gjzxjhService.saveBatch(gjzxjhDaos);
        return true;
    }
}
