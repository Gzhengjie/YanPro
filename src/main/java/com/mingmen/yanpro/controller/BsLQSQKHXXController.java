package com.mingmen.yanpro.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.BsLQSBLDXX;
import com.mingmen.yanpro.dao.BsLQSQKHXX;
import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.service.BsLQSQKHXXService;
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
import java.util.List;

@Api(tags = "博士申请考核信息")
@RestController
@RequestMapping("/BsLQSQKHXX")
public class BsLQSQKHXXController {

    @Autowired
    private BsLQSQKHXXService bsLQSQKHXXService;

    /****
     * 新增/修改
     * @param bsLQSQKHXX
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存", notes = "修改信息")
    public Result save(@RequestBody BsLQSQKHXX bsLQSQKHXX){
        return bsLQSQKHXXService.saveUser(bsLQSQKHXX);
    }

    //查询所有数据
    @GetMapping("/")
    @ApiOperation(value = "查询", notes = "查询所有数据")
    public Result findAll(){
        return Result.success(bsLQSQKHXXService.list());
    }

    /***
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除用户信息")
    public Result delete(@PathVariable Integer id){

        return Result.success(bsLQSQKHXXService.removeById(id));
    }

    /******
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    @ApiOperation(value = "批量删除", notes = "批量删除学生")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        if (bsLQSQKHXXService.removeByIds(ids)) {
            return Result.success();
        }
        else {
            return Result.error(Constants.CODE_500, "系统错误");
        }

    }

    /*******
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
        IPage<BsLQSQKHXX> page = new Page<>(pageNum,pageSize);
        QueryWrapper<BsLQSQKHXX> queryWrapper = new QueryWrapper<>();
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
        return Result.success(bsLQSQKHXXService.page(page,queryWrapper));
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
        QueryWrapper<BsLQSQKHXX> queryWrapper = new QueryWrapper<>();
        if(!"".equals(yxsmc)){
            queryWrapper.like("yxsmc",yxsmc);
        }
        if(nf != 0){
            queryWrapper.eq("nf", nf);
        }
        queryWrapper.eq("shzt", shzt);
        List<BsLQSQKHXX> list = bsLQSQKHXXService.list(queryWrapper);

        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置列宽（列,宽度）
        writer.setColumnWidth(0,15);

        writer.addHeaderAlias("录取院系代码", "录取院系代码");
        writer.addHeaderAlias("院系所名称", "院系所名称");
        writer.addHeaderAlias("专业代码", "专业代码");
        writer.addHeaderAlias("专业名称", "专业名称");
        writer.addHeaderAlias("导师职工号", "导师职工号");
        writer.addHeaderAlias("导师姓名", "导师姓名");
        writer.addHeaderAlias("性别", "性别");
        writer.addHeaderAlias("证件号", "证件号");
        writer.addHeaderAlias("外国语", "外国语");
        writer.addHeaderAlias("业务课一", "业务课一");
        writer.addHeaderAlias("业务课二", "业务课二");
        writer.addHeaderAlias("面试", "面试");
        writer.addHeaderAlias("考核总成绩", "考核总成绩");
        writer.addHeaderAlias("政审结果", "政审结果");
        writer.addHeaderAlias("心里测试结果", "心里测试结果");
        writer.addHeaderAlias("录取类别代码", "录取类别代码");
        writer.addHeaderAlias("录取类别名称", "录取类别名称");
        writer.addHeaderAlias("考生档案所在单位", "考生档案所在单位");
        writer.addHeaderAlias("是否调取档案", "是否调取档案");
        writer.addHeaderAlias("应届硕士生注册学号", "应届硕士生注册学号");
        writer.addHeaderAlias("备注", "备注");
        writer.addHeaderAlias("年份", "年份");

        writer.setOnlyAlias(true);

        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetel.sheet;charset=utf-8");
        String filemane = URLEncoder.encode("考生复试信息", "UTF-8");
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
        List<BsLQSQKHXX> list = reader.readAll(BsLQSQKHXX.class);
        return bsLQSQKHXXService.saveBatch(list, file.getYxsdm(), file.getXm());
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
        QueryWrapper<BsLQSQKHXX> queryWrapper = new QueryWrapper<>();
        BsLQSQKHXX newBsLQSQKHXX = new BsLQSQKHXX();
        queryWrapper.eq("id", id);
        newBsLQSQKHXX.setId(id);
        newBsLQSQKHXX.setShzt(shid);
        newBsLQSQKHXX.setNf(bsLQSQKHXXService.getOne(queryWrapper).getNf());
        if (shid == 1) {
            newBsLQSQKHXX.setScr(TokenUtils.getCurrentUser().getXM());
            newBsLQSQKHXX.setScsj(DateUtil.now());
        }
        else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            newBsLQSQKHXX.setYyshr(TokenUtils.getCurrentUser().getXM());
            newBsLQSQKHXX.setYyshsj(DateUtil.now());
        } else {
            newBsLQSQKHXX.setXyshr(TokenUtils.getCurrentUser().getXM());
            newBsLQSQKHXX.setXyshsj(DateUtil.now());
        }
        return Result.success(bsLQSQKHXXService.updateById(newBsLQSQKHXX));
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
            QueryWrapper<BsLQSQKHXX> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            BsLQSQKHXX  newBsLQSQKHXX = new BsLQSQKHXX();
            newBsLQSQKHXX.setId(id);
            newBsLQSQKHXX.setShzt(shid);
            newBsLQSQKHXX.setNf(bsLQSQKHXXService.getOne(queryWrapper).getNf());
            if (shid == 1) {
                newBsLQSQKHXX.setScr(TokenUtils.getCurrentUser().getXM());
                newBsLQSQKHXX.setScsj(DateUtil.now());
            }
            else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
                newBsLQSQKHXX.setYyshr(TokenUtils.getCurrentUser().getXM());
                newBsLQSQKHXX.setYyshsj(DateUtil.now());
            } else {
                newBsLQSQKHXX.setXyshr(TokenUtils.getCurrentUser().getXM());
                newBsLQSQKHXX.setXyshsj(DateUtil.now());
            }
            bsLQSQKHXXService.updateById(newBsLQSQKHXX);
        }
        return Result.success(true);
    }

    @GetMapping("/tongji")
    @ApiOperation(value = "不同学院不同状态的学生人数", notes = "查找不同学院各个状态的学生")
    public Result tongJi(@RequestParam(defaultValue = "0") int nf) {
        return Result.success(bsLQSQKHXXService.tongJi(nf));
    }

    @GetMapping("/total")
    @ApiOperation(value = "不同状态的学生人数", notes = "查找不同学院不同年份不同状态的学生")
    public Result total(@RequestParam(defaultValue = "") String lqyxdm, @RequestParam(defaultValue = "") String lqyxmc, @RequestParam(defaultValue = "0") int nf) {
        return Result.success(bsLQSQKHXXService.total(lqyxdm,lqyxmc,nf));
    }

    @GetMapping("/zxnf")
    @ApiOperation(value = "生成年份区间", notes = "生成年份区间")
    public Result zxnf() {
        return Result.success(bsLQSQKHXXService.zxnf());
    }

}
