package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SljtgqkDao;
import com.mingmen.yanpro.service.SljtgqkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/sys/data/lqInfo/sljtgqk")
public class SljtgqkController {

    @Autowired
    private SljtgqkService sljtgqkService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody SljtgqkDao sljtgqkDao) {
        //新增或更新
        return Result.success(sljtgqkService.save(sljtgqkDao));
    }

    //查询所有数据
    @GetMapping("/selectAll")
    public Result index() {
        return Result.success(sljtgqkService.findAll());
    }

    //使用通过项目名称进行查询
    @GetMapping("/selectByTGXMMC/{TGXMMC}")
    public Result selectByTgxmmc(@PathVariable String TGXMMC) {
        return Result.success(sljtgqkService.selectByTgxmmc(TGXMMC));
    }

    //删除操作,使用通过项目代码进行删除
//    @DeleteMapping("/delete/{TGXMDM}")  //花括号中TGXMDM与athVariable中TGXMDM需要保持一致
//    public Result delete(@PathVariable String TGXMDM) {
//        return Result.success(sljtgqkService.deleteByTgxmdm(TGXMDM));
//    }

    //删除操作，通过id进行删除
    @DeleteMapping("/delete/{ID}")  //花括号中ID与athVariable中ID需要保持一致
    public Result deleteById(@PathVariable Integer ID) {
        return Result.success(sljtgqkService.deleteById(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(sljtgqkService.deleteBatch(ids));
    }

    //分页查询
    //limit的第一个参数=(pageNum-1)*pageSize
    @GetMapping("/findPage") //接口路径：/sys/data/lqInfo/sljtgqk/findPage
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String TGXMDM,
                           @RequestParam(defaultValue = "") String TGXMMC) {
        pageNum = (pageNum - 1) * pageSize;
        TGXMDM = "%" + TGXMDM + "%";
        TGXMMC = "%" + TGXMMC + "%";
        List<SljtgqkDao> data = sljtgqkService.selectPage(pageNum, pageSize,TGXMDM,TGXMMC);
        Integer total = sljtgqkService.selectTotal(TGXMDM,TGXMMC);
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        Result result = new Result();
        return result.success(res);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        List<SljtgqkDao> list = sljtgqkService.findAll();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(true);
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("TGXMDM", "通过项目代码");
        writer.addHeaderAlias("TGXMMC", "通过项目名称");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("外语等级表", "UTF-8");
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
        //List<SljtgqkDao> list = reader.readAll(SljtgqkDao.class);
        //方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<SljtgqkDao> sljtgqkDaos = CollUtil.newArrayList();
        for(List<Object> row:list) {
            SljtgqkDao sljtgqkDao = new SljtgqkDao();
            sljtgqkDao.setTGXMDM(row.get(0).toString());
            sljtgqkDao.setTGXMMC(row.get(1).toString());
            sljtgqkDaos.add(sljtgqkDao);
        }

        sljtgqkService.saveBatch(sljtgqkDaos);
        return true;
    }
}
