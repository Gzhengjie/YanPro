package com.mingmen.yanpro.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.BsLQSBLDXX;
import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.service.BsLQSBLDXXService;
import com.mingmen.yanpro.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.List;

@Api(tags = "博士硕博连读信息")
@RestController
@RequestMapping("/BsLQSBLDXX")
public class BsLQSBLDXXController {

    @Autowired
    private BsLQSBLDXXService bsLQSBLDXXService;
    // @RequestBody 可以将前台传过来的json对象转化为java对象！！
    // 新增和修改
    @PostMapping("/save")
    @ApiOperation(value = "保存", notes = "修改信息")
    public Result save(@RequestBody BsLQSBLDXX bsLQSBLDXX){
        return bsLQSBLDXXService.saveUser(bsLQSBLDXX);
    }

    //查询所有数据
    @GetMapping("/")
    @ApiOperation(value = "查询", notes = "查询所有数据")
    public Result findAll(){
        return Result.success(bsLQSBLDXXService.list());
    }

    /**
     * 根据id删除学生
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除用户信息")
    public Result delete(@PathVariable Integer id){
        return Result.success(bsLQSBLDXXService.removeById(id));
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    @ApiOperation(value = "批量删除", notes = "批量删除学生")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        if (bsLQSBLDXXService.removeByIds(ids)) {
            return Result.success(true);
        }
        else {
            return Result.error(Constants.CODE_500, "系统错误");
        }

    }

    /****
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param yxsdm 院系所代码
     * @param yxsmc 院系所名称
     * @param zydm 专业代码
     * @param zymc 专业名称
     * @param xh 学号
     * @param xm 姓名
     * @param nf 年份
     * @param shzt 审核状态
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "可查找不同学院、不同状态、不同年份等的学生")
    public Result findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String yxsdm,
                                    @RequestParam(defaultValue = "") String yxsmc,
                                    @RequestParam(defaultValue = "") String zydm,
                                    @RequestParam(defaultValue = "") String zymc,
                                    @RequestParam(defaultValue = "") String xh,
                                    @RequestParam(defaultValue = "") String xm,
                                    @RequestParam(defaultValue = "0") int nf,
                                    @RequestParam(defaultValue = "0") int shzt

    ){
       IPage<BsLQSBLDXX> page = new Page<>(pageNum,pageSize);
        QueryWrapper<BsLQSBLDXX> queryWrapper = new QueryWrapper<>();
        if(!"".equals(yxsdm)){
            queryWrapper.like("yxsdm",yxsdm);
        }
        if(!"".equals(yxsmc)){
            queryWrapper.like("yxsmc",yxsmc);
        }
        if(!"".equals(zydm)){
            queryWrapper.like("zydm",zydm);
        }
        if(!"".equals(zymc)){
            queryWrapper.like("zymc",zymc);
        }
        if(!"".equals(xh)){
            queryWrapper.like("xh",xh);
        }
        if(!"".equals(xm)){
            queryWrapper.like("xm",xm);
        }
        if(nf != 0){
            queryWrapper.eq("nf", nf);
        }
        queryWrapper.eq("shzt", shzt);
        queryWrapper.orderByAsc("yxsdm");
        return Result.success(bsLQSBLDXXService.page(page,queryWrapper));
//        return BsLQSBLDXXService.findPage(new Page<>(pageNum,pageSize), lqyxmc, lqzymc, xxfs, ksbh, ksxm, shzt);
    }

    /****
     * 导出
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    @ApiOperation(value="导出数据", notes="导出审核通过记录")
    public void export(@RequestParam(defaultValue = "")String yxsmc,
                       @RequestParam(defaultValue = "0")int nf,
                       @RequestParam(defaultValue = "4")int shzt,
                       HttpServletResponse response) throws Exception {


        QueryWrapper<BsLQSBLDXX> queryWrapper = new QueryWrapper<>();
        if(!"".equals(yxsmc)){
            queryWrapper.like("yxsmc",yxsmc);
        }
        if(nf != 0){
            queryWrapper.eq("nf", nf);
        }
        queryWrapper.eq("shzt", shzt);
        List<BsLQSBLDXX> list = bsLQSBLDXXService.list(queryWrapper);

        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置列宽（列,宽度）
        writer.setColumnWidth(0,15);

        writer.addHeaderAlias("录取院系代码", "录取院系代码");
        writer.addHeaderAlias("院系所名称", "院系所名称");
        writer.addHeaderAlias("专业代码", "专业代码");
        writer.addHeaderAlias("专业名称", "专业名称");
        writer.addHeaderAlias("学号", "学号");
        writer.addHeaderAlias("姓名", "姓名");
        writer.addHeaderAlias("导师姓名", "导师姓名");
        writer.addHeaderAlias("考生编号", "考生编号");
        writer.addHeaderAlias("导师姓名", "导师姓名");
        writer.addHeaderAlias("应完成学分数", "应完成学分数");
        writer.addHeaderAlias("已完成学分数", "已完成学分数");
        writer.addHeaderAlias("单科最低成绩", "单科最低成绩");
        writer.addHeaderAlias("有无重修", "有无重修");
        writer.addHeaderAlias("有无纪律处分", "有无纪律处分");
        writer.addHeaderAlias("发表论文数", "发表论文数");
        writer.addHeaderAlias("外语考核成绩", "外语考核成绩");
        writer.addHeaderAlias("综合考核评价成绩", "综合考核评价成绩");
        writer.addHeaderAlias("备注", "备注");
        writer.addHeaderAlias("年份", "年份");

        writer.setOnlyAlias(true);

        writer.write(list);
//        writer.writeHeadRow(rowData);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetel.sheet;charset=utf-8");
        String filemane = URLEncoder.encode("硕博连读录取信息汇总", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filemane + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /****
     * 导入
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入数据", notes = "从excel中导入数据")
    public Result imp(UserFileReq file) throws Exception {
        InputStream inputStream = file.getFile().getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<BsLQSBLDXX> list = reader.readAll(BsLQSBLDXX.class);
        return bsLQSBLDXXService.saveBatch(list, file.getYxsdm(), file.getXm());
    }

    /*****
     * 单个审核
     * @param id
     * @param shid
     * @return
     */
    @RequestMapping("/sh")  //单个审核
    @ApiOperation(value = "审核", notes = "根据id和审核码单个审核")
    public Result xxsh(@RequestParam Integer id,
                       @RequestParam Integer shid) {  //输入ID、审核状态

        QueryWrapper<BsLQSBLDXX> queryWrapper = new QueryWrapper<>();
        BsLQSBLDXX newBsLQSBLDXX = new BsLQSBLDXX();
        queryWrapper.eq("id", id);
        newBsLQSBLDXX.setId(id);
        newBsLQSBLDXX.setShzt(shid);
        newBsLQSBLDXX.setNf(bsLQSBLDXXService.getOne(queryWrapper).getNf());
        if (shid == 1) {
            newBsLQSBLDXX.setScr(TokenUtils.getCurrentUser().getXM());
            newBsLQSBLDXX.setScsj(DateUtil.now());
        }
        else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            newBsLQSBLDXX.setYyshr(TokenUtils.getCurrentUser().getXM());
            newBsLQSBLDXX.setYyshsj(DateUtil.now());
        } else {
            newBsLQSBLDXX.setXyshr(TokenUtils.getCurrentUser().getXM());
            newBsLQSBLDXX.setXyshsj(DateUtil.now());
        }
        return Result.success(bsLQSBLDXXService.updateById(newBsLQSBLDXX));
    }

    /*******
     * 批量审核
     * @param ids
     * @param shid
     * @return
     */
    @PostMapping("/sh/batch")
    @ApiOperation(value = "批量审核", notes = "批量审核学生")
    public Result xxbatchSh(@RequestParam List<Integer> ids, @RequestParam Integer shid) {
        for(Integer id : ids){
            QueryWrapper<BsLQSBLDXX> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            BsLQSBLDXX  newBsLQSBLDXX = new BsLQSBLDXX();
            newBsLQSBLDXX.setId(id);
            newBsLQSBLDXX.setShzt(shid);
            newBsLQSBLDXX.setNf(bsLQSBLDXXService.getOne(queryWrapper).getNf());
            if (shid == 1) {
                newBsLQSBLDXX.setScr(TokenUtils.getCurrentUser().getXM());
                newBsLQSBLDXX.setScsj(DateUtil.now());
            }
            else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
                newBsLQSBLDXX.setYyshr(TokenUtils.getCurrentUser().getXM());
                newBsLQSBLDXX.setYyshsj(DateUtil.now());
            } else {
                newBsLQSBLDXX.setXyshr(TokenUtils.getCurrentUser().getXM());
                newBsLQSBLDXX.setXyshsj(DateUtil.now());
            }
            bsLQSBLDXXService.updateById(newBsLQSBLDXX);
        }
        return Result.success(true);
    }

    /***
     * 查找所有的院系所名称
     * @return
     */
    @GetMapping("/alllqyxmc")
    @ApiOperation(value = "院系所名称", notes = "查找所有学院名称")
    public Result findAlllqyxdmc() {
        return Result.success(bsLQSBLDXXService.findAlllqyxmc());
    }

    /*****
     * 根据学院名称查找专业
     * @param yxsmc 学院名称
     * @return
     */
    @GetMapping("/alllqzymc")
    @ApiOperation(value = "专业名称", notes = "查找该学院下的所有专业名称")
    public Result findAlllqzymc(@RequestParam(defaultValue = "") String yxsmc, @RequestParam(defaultValue = "") String yxsdm, @RequestParam(defaultValue = "0") int nf) {
        return Result.success(bsLQSBLDXXService.findAlllqzymc(yxsdm, yxsmc, nf));
    }

    /*****
     * 专业名称查找学习方式
     * @param zymc
     * @return
     */
    @GetMapping("/xxfs")
    public Result findAllxxfs(@RequestParam String zymc) {
        return Result.success(bsLQSBLDXXService.findAllssfx(zymc));
    }

    @GetMapping("/tongji")
    @ApiOperation(value = "不同学院不同状态的学生人数", notes = "查找不同学院各个状态的学生")
    public Result tongJi(@RequestParam(defaultValue = "0") int nf) {
        return Result.success(bsLQSBLDXXService.tongJi(nf));
    }

    @GetMapping("/total")
    @ApiOperation(value = "不同状态的学生人数", notes = "查找不同学院不同年份不同状态的学生")
    public Result total(@RequestParam(defaultValue = "") String lqyxdm, @RequestParam(defaultValue = "") String lqyxmc, @RequestParam(defaultValue = "0") int nf) {
        return Result.success(bsLQSBLDXXService.total(lqyxdm,lqyxmc,nf));
    }

    @GetMapping("/zxnf")
    @ApiOperation(value = "生成年份区间", notes = "生成年份区间")
    public Result zxnf() {
        return Result.success(bsLQSBLDXXService.zxnf());
    }

    @GetMapping("/getyxsdm")
    public Result getYxsdm(@RequestParam(defaultValue = "")String yxsmc) {
        return Result.success(bsLQSBLDXXService.getYxsdm(yxsmc));
    }

}
