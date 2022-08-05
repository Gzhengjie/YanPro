package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SscskmDao;
import com.mingmen.yanpro.dao.YXSDao;
import com.mingmen.yanpro.service.SscskmService;
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
@RequestMapping("/SSCSKM")
public class SscskmController {

    @Autowired
    private SscskmService sscskmService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody SscskmDao sscskmDao){
        //新增或者更新
        Result result = new Result();
        return result.success(sscskmService.save(sscskmDao));
    }

    //查询所有
    @GetMapping("/selectAll")
    public Result selectAll(){
        Result result = new Result();
        return result.success(sscskmService.selectAll());
    }

    //按院系所名称查询
    @GetMapping("/selectByMC")
    public Result selectByMC(@RequestParam String KSKMMC) {
        Result result = new Result();
        return result.success(sscskmService.selectByMC(KSKMMC));
    }

    //删除
    @DeleteMapping("/delete/{ID}")
    public Result delete(@PathVariable String ID){
        Result result = new Result();
        return result.success(sscskmService.deleteByID(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<String> IDs) {
        Result result = new Result();
        return result.success(sscskmService.deleteBatch(IDs));

    }

    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查出所有数据
        List<SscskmDao> list = sscskmService.selectAll();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("KSKMDM","考试科目码");
        writer.addHeaderAlias("KSKMMC","考试科目名称");
        writer.addHeaderAlias("MTYXSDM","命题院系所代码");
        writer.addHeaderAlias("CKSM","考试范围");
        writer.addHeaderAlias("KSFZ","考试分值");
        writer.addHeaderAlias("KSSC","考试时长");
        writer.addHeaderAlias("SFDHB","是否带画板");
        writer.addHeaderAlias("KSSM","考试说明");
        writer.addHeaderAlias("SFTK","是否统考");
        writer.addHeaderAlias("KSDY","考试单元");

        //一次性写出list内对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("硕士考试科目表","UTF-8");
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
        List<SscskmDao> sscskmDaos = CollUtil.newArrayList();
        for (List<Object> row : list){
            SscskmDao sscskmDao = new SscskmDao();
            sscskmDao.setKSKMDM(row.get(0).toString());
            sscskmDao.setKSKMMC(row.get(1).toString());
            sscskmDao.setMTYXSDM(row.get(2).toString());
            sscskmDao.setCKSM(row.get(3).toString());
            sscskmDao.setKSFZ(row.get(4).toString());
            sscskmDao.setKSSC(row.get(5).toString());
            sscskmDao.setSFDHB(row.get(6).toString());
            sscskmDao.setKSSM(row.get(7).toString());
            sscskmDao.setSFTK(row.get(8).toString());
            sscskmDao.setKSDY(row.get(9).toString());
            sscskmDaos.add(sscskmDao);
        }
        sscskmService.saveBatch(sscskmDaos);
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
        List<SscskmDao> data = sscskmService.selectPage(pageNum,pageSize);
        Integer total = sscskmService.selectTotal();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        Result result = new Result();
        return result.success(res);
    }
}
