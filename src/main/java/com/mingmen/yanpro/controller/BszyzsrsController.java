package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.BszsjzDao;
import com.mingmen.yanpro.dao.BszyzsrsDao;
import com.mingmen.yanpro.dao.UserDao;
import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.service.BszsjzService;
import com.mingmen.yanpro.service.BszyzsrsService;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor/jz/rs")
public class BszyzsrsController {

    @Autowired
    private BszyzsrsService bszyzsrsService;
    @Autowired
    private BszsjzService bszsjzService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody BszyzsrsDao bszyzsrsDao) {
        //新增或更新
        if(bszyzsrsService.saveBszyzsrs(bszyzsrsDao)==0){
            Result result = new Result();
            result.setCode("401");
            result.setMsg("当前年份该专业已存在，无法新增");
            return result;
        }
        else{
            return Result.success(bszyzsrsService.saveBszyzsrs(bszyzsrsDao));
        }
    }

    //查询全部操作
    @GetMapping("/selectAll")
    public Result index() {
        return Result.success(bszyzsrsService.selectAll());
    }

    //通过院系所名称查询院系所代码
    @GetMapping("/selectzy")
    public Result selectByYXSMC(@RequestParam String YXSMC) {
        return Result.success(bszyzsrsService.selectByYXSMC(YXSMC));
    }

    //返回年份最小值
    @GetMapping("/minyear")
    public Result minYear() {
        UserDao userDao = TokenUtils.getCurrentUser();
        return Result.success(bszyzsrsService.minYear(userDao.getYXSDM()));
    }

    //通过专业名称查询拟招生人数和年份
    @GetMapping("/selectrsnf")
    public Result selectByZYMC(@RequestParam String ZYMC) {return Result.success(bszyzsrsService.selectByZYMC(ZYMC));}

    //删除操作,通过id进行删除
    @DeleteMapping("/delete/{ID}")
    public Result deleteById(@PathVariable Integer ID) {
        return Result.success(bszyzsrsService.deleteById(ID));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        Result result = new Result();
        for(Integer id:ids){
            int czm = bszyzsrsService.getNFCZ(id);
            if(czm==2){
                result.setCode("401");
                result.setMsg("所选择数据中含有待学院领导审核的数据，无法批量删除");
                return result;
            }
            else if(czm==3){
                result.setCode("401");
                result.setMsg("所选择的数据含有学院领导审核通过的数据，无法批量删除");
                return result;
            }
            else if(czm==4){
                result.setCode("401");
                result.setMsg("所选择的数据含有研究生院审核完成的数据，无法批量删除");
                return result;
            }
            else if(czm==0){
                result.setCode("401");
                result.setMsg("所选择的数据中含有未在当前年份的数据，无法批量删除");
                return result;
            }
            else if(czm==1){
                result.setCode("200");
            }
            else{
                result.setCode("401");
                result.setMsg("系统错误");
                return result;
            }
        }
        return Result.success(bszyzsrsService.deleteBatch(ids));
    }

    //返回博士招生专业表中专业代码的集合
    @GetMapping("/allzydm")
    public Result allZYDM(){
        return Result.success(bszsjzService.allZydm(TokenUtils.getCurrentUser().getYXSDM()));
    }

    //返回博士招生专业表中专业名称的集合
    @GetMapping("/allzymc")
    public Result allZYMC() {
        return Result.success(bszsjzService.allZymc(TokenUtils.getCurrentUser().getYXSDM()));
    }

    //判断能否单个编辑和当个删除
    @GetMapping("/getNFCZ")
    public Result getNFCZ(@RequestParam Integer id) {
        Result result = new Result();
        int czm = bszyzsrsService.getNFCZ(id);
        if(czm==0){
            result.setCode("401");
            result.setMsg("未在当前年份");
            return result;
        }
        else if(czm==1){
            result.setCode("200");
            return result;
        }
        else if(czm==2){
            result.setCode("401");
            result.setMsg("已上传，学院领导审核中");
            return result;
        }
        else if(czm==3){
            result.setCode("401");
            result.setMsg("研究生院审核中");
            return result;
        }
        else if(czm==4){
            result.setCode("401");
            result.setMsg("简章编制已完成");
            return result;
        }
        else if(czm==5){
            result.setCode("401");
            result.setMsg("系统错误");
            return result;
        }
        return Result.success();
    }

    //判断学院领导能否填写备注
    @GetMapping("/getxyldNFCZ")
    public Result getxyldNFCZ(@RequestParam Integer id) {
        Result result = new Result();
        int czm = bszyzsrsService.getxyldNFCZ(id);
        if(czm==0){
            result.setCode("401");
            result.setMsg("学院秘书尚未提交");
            return result;
        }
        else if(czm==1){
            result.setCode("200");
            return result;
        }
        else if(czm==2){
            result.setCode("401");
            result.setMsg("已经完成审核，无法修改");
            return result;
        }
        else if(czm==3){
            result.setCode("401");
            result.setMsg("未在当前年份");
            return result;
        }
        else{
            result.setCode("401");
            result.setMsg("系统错误");
            return result;
        }
    }

    //判断学院博士专业招生人数表能否导入
    @GetMapping("/getNFSC")
    public Result getNFSC(@RequestParam String yxsdm,@RequestParam String nf) {
        if(bszyzsrsService.getNFSC(yxsdm,nf)==true){
            return Result.success(true);
        }
        else{
            Result result = new Result();
            result.setCode("401");
            result.setData("false");
            return result;
        }
    }

    /**
     * 分页查询
     * @param pageNum 当前页
     * @param pageSize 每页数据数目
     * @param yxsdm 院系所代码
     * @param yxsmc 院系所名称
     * @param zydm 专业代码
     * @param zymc 专业名称
     * @param nf 年份
     * @return
     */
    @GetMapping("/findPage")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String yxsdm,
                           @RequestParam(defaultValue = "") String yxsmc,
                           @RequestParam(defaultValue = "") String zydm,
                           @RequestParam(defaultValue = "") String zymc,
                           @RequestParam(defaultValue = "") String nf){
            IPage<BszyzsrsDao> page = bszyzsrsService.findPage(new Page<>(pageNum, pageSize), yxsdm, yxsmc, zydm, zymc, nf);
            return  Result.success(page);
    }

    /**
     * 导出接口
     */
    @GetMapping("/mldc/export")
    public void export(HttpServletResponse response,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String yxsdm,
                       @RequestParam(defaultValue = "") String yxsmc,
                       @RequestParam(defaultValue = "") String zydm,
                       @RequestParam(defaultValue = "") String zymc,
                       @RequestParam(defaultValue = "") String nf) throws Exception {
        //从数据库查询出所有数据
        IPage<BszyzsrsDao> bszyrs = bszyzsrsService.findPage(new Page<>(pageNum,pageSize),yxsdm,yxsmc,zydm,zymc,nf);
        List<BszyzsrsDao> list = bszyrs.getRecords();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("YXSDM", "院系所代码");
        writer.addHeaderAlias("YXSMC", "院系所名称");
        writer.addHeaderAlias("ZYDM", "专业代码");
        writer.addHeaderAlias("ZYMC", "专业名称");
        writer.addHeaderAlias("NZSRS", "拟招生人数");
        writer.addHeaderAlias("NF", "年份");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("学院博士专业招生人数表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /**
     * excel 导入
     * @param userFileReq
     * @throws Exception
     */
    @PostMapping("/mlbz/import")
    public Result imp(UserFileReq userFileReq) throws Exception {
        if(this.getNFSC(userFileReq.getYxsdm(),userFileReq.getNf()).getData().toString().equals("false")){
            Result result = new Result();
            result.setCode("401");
            result.setMsg("当前不能上传");
            return result;
        }
        InputStream inputStream = userFileReq.getFile().getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式1：通过JavaBean的方式读取Excel内的对象，但是要求表头必须是英文，跟JavaBean的属性要对应起来
        //List<BszyzsrsDao> list = reader.readAll(BszyzsrsDao.class);
        //方式2：忽略表头的中文，直接读取表的内容
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR) + 1);
        List<List<Object>> list = reader.read(1);
        List<BszyzsrsDao> bszyzsrsDaos = CollUtil.newArrayList();
        for(List<Object> row:list) {
            BszyzsrsDao bszyzsrsDao = new BszyzsrsDao();
            bszyzsrsDao.setYXSDM(userFileReq.getYxsdm());
            bszyzsrsDao.setYXSMC(bszsjzService.getyxsdmandmc(userFileReq.getYxsdm()));
            bszyzsrsDao.setZYDM(row.get(0).toString());
            bszyzsrsDao.setZYMC(row.get(1).toString());
            bszyzsrsDao.setNZSRS((Integer.valueOf(row.get(2).toString())));
            bszyzsrsDao.setNF(year);
            bszyzsrsDao.setSCR(userFileReq.getXm());
            bszyzsrsDaos.add(bszyzsrsDao);
        }
        
        List<BszyzsrsDao> saveornot = bszyzsrsService.saveBatch(bszyzsrsDaos);
        if(saveornot.size()>0){
            Integer del = bszyzsrsDaos.size() - saveornot.size();
            Result result = new Result();
            result.setCode("401");
            result.setMsg("应导入数据总计"+bszyzsrsDaos.size()+"条，实际导入"+ del + "条,错误信息已下载");
            result.setData(saveornot);
            return result;
        }else {
            return Result.success();
        }
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/loadmb")
    public void loadmb(HttpServletResponse response) throws Exception {
        IPage<BszyzsrsDao> bszsjz = bszyzsrsService.findPage(new Page<>(1,1),"", "", "", "", "");
        List<BszyzsrsDao> list = bszsjz.getRecords();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("ZYDM", "专业代码");
        writer.addHeaderAlias("ZYMC", "专业名称");
        writer.addHeaderAlias("NZSRS", "拟招生人数");

        writer.setOnlyAlias(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("博士学院专业招生人数导入模板", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}
