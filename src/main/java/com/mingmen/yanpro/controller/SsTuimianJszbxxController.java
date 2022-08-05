package com.mingmen.yanpro.controller;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsTuimianJsxx;
import com.mingmen.yanpro.dao.SsTuimianJszbxx;
import com.mingmen.yanpro.mapper.SsTuimianJszbxxMapper;
import com.mingmen.yanpro.service.SsTuimianJszbxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/tuimian_jszbxx")
public class SsTuimianJszbxxController {
    @Autowired
    private SsTuimianJszbxxMapper ssTuimianJszbxxMapper;

    @Autowired
    private SsTuimianJszbxxService ssTuimianJszbxxService;

    // 查询所有数据
    @GetMapping("/findall")
    public Result index(){
        return Result.success(ssTuimianJszbxxService.list());
    }

    /**
     *
     *
     * 新增和修改
     *
     *
     */
    @PostMapping("/save")
    public Result save(@RequestBody SsTuimianJszbxx ssTuimianJszbxx){
        //新增或者更新
        if (ssTuimianJszbxxService.saveSsTuimianJszbxx(ssTuimianJszbxx))
            return Result.success(true);
        else
            return Result.error(Constants.CODE_500, "该学生已存在或系统错误");

    }
    /**
     *
     *
     * 根据id删除学生
     *
     *
     */
    @DeleteMapping("/del/{id}")
    public boolean delete(@PathVariable Integer id){
        return ssTuimianJszbxxService.removeById(id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        if (ssTuimianJszbxxService.removeByIds(ids)) {
            return Result.success();
        }
        else {
            return Result.error(Constants.CODE_500, "系统错误");
        }

    }
    //分页查询 - mybatis-plus的方式
    /**
     *
     *
     * 分页查询
     *
     *
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "0") int nf,
                           @RequestParam String yxsdm,
                           @RequestParam(defaultValue = "") String shzt
    ){
        IPage<SsTuimianJszbxx> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SsTuimianJszbxx> queryWrapper = new QueryWrapper<>();
        if(nf!=0){
            queryWrapper.eq("nf",nf);
        }
        if(!"".equals(yxsdm))
        {
            queryWrapper.eq("jsyxsdm",yxsdm);
        }
        queryWrapper.eq("shzt",shzt);
        return Result.success(ssTuimianJszbxxService.page(page, queryWrapper));
    }



    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam String yxsdm,
                       @RequestParam(defaultValue = "") Integer shzt,
                       @RequestParam(defaultValue = "0") int nf
    ) throws Exception{
        //从数据库查询出所有的数据
        List<SsTuimianJszbxx> list = ssTuimianJszbxxService.list();

        Iterator<SsTuimianJszbxx> it = list.iterator();
        while(it.hasNext())
        {
            SsTuimianJszbxx sss = it.next();
            String yxs = sss.getJsyxsdm();
            Integer zt = sss.getShzt();
            int nf1 = sss.getNf();
            if(!yxsdm.equals("") && !yxs.equals(yxsdm))
            {
                it.remove();
            }
            else if(shzt!=0 &&!zt.equals(shzt))
            {
                it.remove();
            }
            else if(nf!=0 && nf!=nf1)
            {
                it.remove();
            }
        }

        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名

        // 一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("接收直博生汇总", "utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
    /**
     *
     *
     *
     * 批量选择导出
     *
     *
     *
     */
    @GetMapping("/export1")
    public void export1(HttpServletResponse response,
                        @RequestParam List<Integer> ids
    ) throws Exception{
        //从数据库查询出所有的数据
        List<SsTuimianJszbxx> list = ssTuimianJszbxxService.list();

        Iterator<SsTuimianJszbxx> it = list.iterator();
        while(it.hasNext())
        {
            SsTuimianJszbxx sss = it.next();
            Integer id=sss.getId();
            if(!ids.contains(id))
            {
                it.remove();
                //  System.out.println(id);
            }
        }
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("接收直博推免生汇总", "utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
    /**
     *
     *
     *
     * excel 导入
     *
     *
     */
    @PostMapping("/import")
    public Boolean imp(@RequestParam MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<SsTuimianJszbxx> list = reader.readAll(SsTuimianJszbxx.class);
        /*
        List<List<Object>> list = reader.read(1);
        List<SsTuimianJszbxx> ssTuimianJszbxxes = CollUtil.newArrayList();
        for (List<Object> row : list) {
            SsTuimianJszbxx ssTuimianJszbxx = new SsTuimianJszbxx();
            ssTuimianJszbxx.setXm(row.get(0).toString());
            ssTuimianJszbxx.setXb(row.get(1).toString());
            ssTuimianJszbxx.setTjyxsdm(row.get(2).toString());
            ssTuimianJszbxx.setTjyxsmc(row.get(3).toString());
            ssTuimianJszbxx.setBkzydm(row.get(4).toString());
            ssTuimianJszbxx.setBkzymc(row.get(5).toString());
            ssTuimianJszbxx.setJsyxsdm(row.get(6).toString());
            ssTuimianJszbxx.setJsyxsmc(row.get(7).toString());
            ssTuimianJszbxx.setNgdzydm(row.get(8).toString());
            ssTuimianJszbxx.setNgdzymc(row.get(9).toString());
            ssTuimianJszbxx.setDsxm(row.get(10).toString());
            ssTuimianJszbxx.setDssfbd(row.get(11).toString());
            ssTuimianJszbxx.setJslxdm(row.get(12).toString());
            ssTuimianJszbxx.setJslxmc(row.get(13).toString());
            ssTuimianJszbxx.setFszcj(Float.parseFloat(row.get(14).toString()));
            ssTuimianJszbxx.setFszcjpm(Integer.parseInt(row.get(15).toString()));
            ssTuimianJszbxx.setWydj(row.get(16).toString());
            ssTuimianJszbxx.setJslbdm(row.get(17).toString());
            ssTuimianJszbxx.setJslbmc(row.get(18).toString());
            ssTuimianJszbxx.setTmsly(row.get(19).toString());
            ssTuimianJszbxx.setCjxly(row.get(20).toString());
            ssTuimianJszbxx.setShzt(Integer.parseInt(row.get(23).toString()));
            ssTuimianJszbxx.setNf(Integer.parseInt(row.get(22).toString()));
            ssTuimianJszbxx.setScr(row.get(24).toString());
            ssTuimianJszbxxes.add(ssTuimianJszbxx);
        }
 */
        inputStream.close();
        ssTuimianJszbxxService.saveBatch(list);
        return true;
    }

    /*****
     *
     * 单个审核
     *
     *
     */
    @RequestMapping("/sh")  //单个审核
    public Result xxsh(@RequestParam Integer id,
                       @RequestParam Integer shzt) {  //输入ID、审核状态
        SsTuimianJszbxx ssTuimianJszbxx = new SsTuimianJszbxx();
        ssTuimianJszbxx.setId(id);
        ssTuimianJszbxx.setShzt(shzt);
        return Result.success(ssTuimianJszbxxService.updateById(ssTuimianJszbxx));
    }

    /*******
     *
     * 批量审核
     *
     */
    @PostMapping("/sh/batch")
    public Result xxbatchSh(@RequestParam List<Integer> ids,
                            @RequestParam Integer shzt) {
        for(Integer id : ids){
            SsTuimianJszbxx ssTuimianJszbxx = new SsTuimianJszbxx();
            ssTuimianJszbxx.setId(id);
            ssTuimianJszbxx.setShzt(shzt);
            ssTuimianJszbxxService.updateById(ssTuimianJszbxx);
        }
        return Result.success(true);
    }

    //jsyxsdm
    @GetMapping("/tongji")
    public Result tongJi(@RequestParam(defaultValue = "0") int nf) {
        return Result.success(ssTuimianJszbxxService.tongJi(nf));
    }
}
