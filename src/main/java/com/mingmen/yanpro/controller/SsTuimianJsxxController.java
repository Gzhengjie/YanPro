package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsTuimianJsxx;
import com.mingmen.yanpro.mapper.SsTuimianJsxxMapper;
import com.mingmen.yanpro.service.SsTuimianJsxxService;
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
@RequestMapping("/tuimian_jsxx")
public class SsTuimianJsxxController {
    @Autowired
    private SsTuimianJsxxMapper ssTuimianJsxxMapper;

    @Autowired
    private SsTuimianJsxxService ssTuimianJsxxService;

    /**
     *
     *
     * 新增和修改
     *
     *
     */
    @PostMapping("/save")
    public Result save(@RequestBody SsTuimianJsxx ssTuimianJsxx){
        //新增或者更新
        if (ssTuimianJsxxService.saveSsTuimianJsxx(ssTuimianJsxx))
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
        return ssTuimianJsxxService.removeById(id);
    }


    /**
     *
     *
     * 批量删除
     *
     *
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestParam List<Integer> ids){
        if (ssTuimianJsxxService.removeByIds(ids)) {
            return Result.success();
        }
        else {
            return Result.error(Constants.CODE_500, "系统错误");
        }
    }

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
                           @RequestParam(defaultValue = "") String tmsly,
                           @RequestParam(defaultValue = "0") int nf,
                           @RequestParam(defaultValue = "") String yxsdm,
                           @RequestParam(defaultValue = "") String shzt
    ){
        IPage<SsTuimianJsxx> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SsTuimianJsxx> queryWrapper = new QueryWrapper<>();
        if(!"".equals(tmsly)){
            queryWrapper.like("tmsly",tmsly);
        }
        if(nf!=0){
            queryWrapper.eq("nf",nf);
        }
        if(!"".equals(yxsdm))
        {
            queryWrapper.eq("tjyxsdm",yxsdm);
        }
        queryWrapper.eq("shzt",shzt);
        return Result.success(ssTuimianJsxxService.page(page, queryWrapper));
    }



    /**
     *
     *
     *
     * 导出接口
     *
     *
     *
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(defaultValue = "") String yxsdm,
                       @RequestParam(defaultValue = "") Integer shzt,
                       @RequestParam(defaultValue = "") String tmsly,
                       @RequestParam(defaultValue = "0") int nf
    ) throws Exception{
        //从数据库查询出所有的数据
        List<SsTuimianJsxx> list = ssTuimianJsxxService.list();

        Iterator<SsTuimianJsxx> it = list.iterator();
        while(it.hasNext())
        {
            SsTuimianJsxx sss = it.next();
            String yxs = sss.getJsyxsdm();
            Integer zt = sss.getShzt();
            int nf1 = sss.getNf();
            String tms = sss.getTmsly();
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
            else if(!tms.equals("") && !tms.contains(tmsly))
            {
                it.remove();
            }
        }
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("接收推免生汇总", "utf-8");
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
        List<SsTuimianJsxx> list = ssTuimianJsxxService.list();

        Iterator<SsTuimianJsxx> it = list.iterator();
        while(it.hasNext())
        {
            SsTuimianJsxx sss = it.next();
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
        String fileName = URLEncoder.encode("接收推免生汇总", "utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     *
     *
     * excel 导入
     *
     *
     *
     */
    @PostMapping("/import")
    public Boolean imp(@RequestParam MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
       // List<SsTuimianJsxx> list = reader.readAll(SsTuimianJsxx.class);

        List<List<Object>> list = reader.read(1);
        List<SsTuimianJsxx> ssTuimianJsxxes = CollUtil.newArrayList();
        for (List<Object> row : list) {
            SsTuimianJsxx ssTuimianJsxx = new SsTuimianJsxx();
            ssTuimianJsxx.setXm(row.get(0).toString());
            ssTuimianJsxx.setXb(row.get(1).toString());
            ssTuimianJsxx.setTjyxsdm(row.get(2).toString());
            ssTuimianJsxx.setTjyxsmc(row.get(3).toString());
            ssTuimianJsxx.setBkzydm(row.get(4).toString());
            ssTuimianJsxx.setBkzymc(row.get(5).toString());
            ssTuimianJsxx.setJsyxsdm(row.get(6).toString());
            ssTuimianJsxx.setJsyxsmc(row.get(7).toString());
            ssTuimianJsxx.setNgdzydm(row.get(8).toString());
            ssTuimianJsxx.setNgdzymc(row.get(9).toString());
            ssTuimianJsxx.setDsxm(row.get(10).toString());
            ssTuimianJsxx.setDssfbd(row.get(11).toString());
            ssTuimianJsxx.setJslxdm(row.get(12).toString());
            ssTuimianJsxx.setJslxmc(row.get(13).toString());
            ssTuimianJsxx.setFszcj(Float.parseFloat(row.get(14).toString()));
            ssTuimianJsxx.setFszcjpm(Integer.parseInt(row.get(15).toString()));
            ssTuimianJsxx.setWydj(row.get(16).toString());
            ssTuimianJsxx.setJslbdm(row.get(17).toString());
            ssTuimianJsxx.setJslbmc(row.get(18).toString());
            ssTuimianJsxx.setTmsly(row.get(19).toString());
            ssTuimianJsxx.setCjxly(row.get(20).toString());
            ssTuimianJsxx.setShzt(Integer.parseInt(row.get(23).toString()));
            ssTuimianJsxx.setNf(Integer.parseInt(row.get(22).toString()));
            ssTuimianJsxx.setScr(row.get(24).toString());
            ssTuimianJsxx.setYyshr(row.get(27).toString());
            ssTuimianJsxxes.add(ssTuimianJsxx);
        }
        inputStream.close();
        ssTuimianJsxxService.saveBatch(ssTuimianJsxxes);
        return true;
    }
    /*****
     *
     *
     *
     * 单个审核
     *
     *
     *
     */
    @RequestMapping("/sh")  //单个审核
    public Result xxsh(@RequestParam Integer id,
                       @RequestParam Integer shzt) {  //输入ID、审核状态
        QueryWrapper<SsTuimianJsxx> queryWrapper = new QueryWrapper<>();
        SsTuimianJsxx newssTuimianJsxx = new SsTuimianJsxx();
        newssTuimianJsxx.setId(id);
        newssTuimianJsxx.setShzt(shzt);
        /*
        newssTuimianJsxx.setNf(ssTuimianJsxxService.getOne(queryWrapper).getNf());
        if(shzt == 1)
        {
            newssTuimianJsxx.setScr(TokenUtils.getCurrentBaseUser().getXm());
            newssTuimianJsxx.setScsj(DateUtil.now());
        }
        else if("1-1".equals(TokenUtils.getCurrentBaseUser().getYhz()))
        {
            newssTuimianJsxx.setYyshr(TokenUtils.getCurrentBaseUser().getXm());
            newssTuimianJsxx.setYyshsj(DateUtil.now());
        }
        else
        {
            newssTuimianJsxx.setXyshr(TokenUtils.getCurrentBaseUser().getXm());
            newssTuimianJsxx.setXyshsj(DateUtil.now());
        }

         */
        return Result.success(ssTuimianJsxxService.updateById(newssTuimianJsxx));
    }

    /*******
     *
     *
     *
     * 批量审核
     *
     *
     *
     */
    @PostMapping("/sh/batch")
    public Result xxbatchSh(@RequestParam List<Integer> ids,
                            @RequestParam Integer shzt) {
        for(Integer id : ids){
            SsTuimianJsxx ssTuimianJsxx = new SsTuimianJsxx();
            ssTuimianJsxx.setId(id);
            ssTuimianJsxx.setShzt(shzt);
            ssTuimianJsxxService.updateById(ssTuimianJsxx);
        }
        return Result.success(true);
    }

    @GetMapping("/tongji")
    public Result tongJi(@RequestParam(defaultValue = "0") int nf) {
        return Result.success(ssTuimianJsxxService.tongJi(nf));
    }
}
