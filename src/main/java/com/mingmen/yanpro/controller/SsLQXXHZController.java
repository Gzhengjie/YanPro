package com.mingmen.yanpro.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.SsLQXXHZ;
import com.mingmen.yanpro.dao.UserFileReq;
import com.mingmen.yanpro.utils.TokenUtils;
import com.mingmen.yanpro.service.SsLQXXHZService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

@Api(tags = "硕士录取信息汇总")
@RestController
@RequestMapping("/SsLQXXHZ")
public class SsLQXXHZController {

    @Autowired
    private SsLQXXHZService ssLQXXHZService;

    // @RequestBody 可以将前台传过来的json对象转化为java对象！！
    // 新增和修改
    @ApiOperation(value = "保存", notes = "修改信息")
    @PostMapping("/save")
    public Result save(@RequestBody SsLQXXHZ ssLQXXHZ){

        if (ssLQXXHZService.saveUser(ssLQXXHZ))
            return Result.success(true);
        else
            return Result.error(Constants.CODE_500, "系统错误");
    }

    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping("/add")
    public Result add(@RequestBody SsLQXXHZ ssLQXXHZ) {
        return ssLQXXHZService.add(ssLQXXHZ);
    }

    //查询所有数据
    @ApiOperation(value = "查询", notes = "查询所有数据")
    @GetMapping("/")
    public Result findAll(){
        return Result.success(ssLQXXHZService.list());
    }

    /**
     * 根据id删除学生
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除用户信息")
    public Result delete(@PathVariable Integer id){

        return Result.success(ssLQXXHZService.removeById(id));
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    @ApiOperation(value = "批量删除", notes = "批量删除学生")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        if (ssLQXXHZService.removeByIds(ids)) {
            return Result.success();
        }
        else {
            return Result.error(Constants.CODE_500, "系统错误");
        }

    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param lqyxdm 录取院系代码
     * @param lqyxmc 录取院系名称
     * @param lqzymc 录取专业名称
     * @param xxfs 学习方式
     * @param zxjh 专项计划
     * @param ksbh 考生编号
     * @param ksxm 考生姓名
     * @param nf   年份
     * @param shzt 审核状态
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "可查找不同学院、不同状态、不同年份等的学生")
    public Result findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String lqyxdm,
                                    @RequestParam(defaultValue = "") String lqyxmc,
                                    @RequestParam(defaultValue = "") String lqzymc,
                                    @RequestParam(defaultValue = "") String xxfs,
                                    @RequestParam(defaultValue = "") String zxjh,
                                    @RequestParam(defaultValue = "") String ksbh,
                                    @RequestParam(defaultValue = "") String ksxm,
                                    @RequestParam(defaultValue = "0") int nf,
                                    @RequestParam(defaultValue = "0") int shzt
    ){
        return Result.success(ssLQXXHZService.findPage(new Page<>(pageNum,pageSize), lqyxdm, lqyxmc, lqzymc, xxfs, zxjh, ksbh, ksxm, nf, shzt));
    }

    /**
     * 导出
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    @ApiOperation(value="导出数据", notes="导出审核通过记录")
    @ApiParam(name = "", value = "", required = false)
    public void export(@RequestParam(defaultValue = "")String yxsmc,
                       @RequestParam(defaultValue = "0")int nf,
                       @RequestParam(defaultValue = "4")int shzt,
                       HttpServletResponse response) throws Exception {
        ssLQXXHZService.export(yxsmc, nf, shzt, response);
    }

    /**
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
        List<SsLQXXHZ> list = reader.readAll(SsLQXXHZ.class);
        return ssLQXXHZService.saveBatch(list, file.getYxsdm(), file.getXm());
    }

    /**
     * 单个审核
     * @param id
     * @param shid
     * @return
     */
    @RequestMapping("/sh")  //单个审核
    @ApiOperation(value = "审核", notes = "根据id和审核码单个审核")
    public Result xxsh(@RequestParam Integer id,
                      @RequestParam Integer shid, @RequestParam(defaultValue = "") String msg) {  //输入ID、审核状态
        QueryWrapper<SsLQXXHZ> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SsLQXXHZ newSsLQXXHZ = new SsLQXXHZ();
        newSsLQXXHZ.setId(id);
        newSsLQXXHZ.setShzt(shid);
        newSsLQXXHZ.setNf(ssLQXXHZService.getOne(queryWrapper).getNf());
        newSsLQXXHZ.setBz(msg);
        if (shid == 1) {
            newSsLQXXHZ.setScr(TokenUtils.getCurrentUser().getXM());
            newSsLQXXHZ.setScsj(DateUtil.now());
        }
        else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            newSsLQXXHZ.setYyshr(TokenUtils.getCurrentUser().getXM());
            newSsLQXXHZ.setYyshsj(DateUtil.now());
        } else {
            newSsLQXXHZ.setXyshr(TokenUtils.getCurrentUser().getXM());
            newSsLQXXHZ.setXyshsj(DateUtil.now());
        }
        return Result.success(ssLQXXHZService.updateById(newSsLQXXHZ));
    }

    /**
     * 批量审核
     * @param ids
     * @param shid
     * @return
     */
    @PostMapping("/sh/batch")
    @ApiOperation(value = "批量审核", notes = "批量审核学生")
    public Result xxbatchSh(@RequestParam List<Integer> ids, @RequestParam Integer shid) {
        for(Integer id : ids){
            QueryWrapper<SsLQXXHZ> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            SsLQXXHZ newSsLQXXHZ = new SsLQXXHZ();
            newSsLQXXHZ.setId(id);
            newSsLQXXHZ.setShzt(shid);
            newSsLQXXHZ.setNf(ssLQXXHZService.getOne(queryWrapper).getNf());
            if (shid == 1) {
                newSsLQXXHZ.setScr(TokenUtils.getCurrentUser().getXM());
                newSsLQXXHZ.setScsj(DateUtil.now());
            }
            else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
                newSsLQXXHZ.setYyshr(TokenUtils.getCurrentUser().getXM());
                newSsLQXXHZ.setYyshsj(DateUtil.now());
            } else {
                newSsLQXXHZ.setXyshr(TokenUtils.getCurrentUser().getXM());
                newSsLQXXHZ.setXyshsj(DateUtil.now());
            }
            ssLQXXHZService.updateById(newSsLQXXHZ);
        }
        return Result.success(true);
    }

    /****
     * 查找所有学院名称
     * @return
     */
    @GetMapping("/alllqyxmc")
    @ApiOperation(value = "院系所名称", notes = "查找所有学院名称")
    public Result findAlllqyxdmc() {
        return Result.success(ssLQXXHZService.findAlllqyxmc());
    }

    /*****
     * 根据学院名称查找专业
     * @param lqyxdm 学院名称
     * @return
     */
    @GetMapping("/alllqzymc")
    @ApiOperation(value = "专业名称", notes = "查找该学院下的所有专业名称")
    public Result findAlllqzymc(@RequestParam String lqyxdm, @RequestParam(defaultValue = "0") int nf) {
        return Result.success(ssLQXXHZService.findAlllqzymc(lqyxdm, nf));
    }

    /*****
     * 专业名称查找学习方式
     * @param zymc
     * @return
     */
    @GetMapping("/xxfs")
    @ApiOperation(value = "学习方式", notes = "查找不同专业的学习方式")
    public Result findAllxxfs(@RequestParam String zymc) {
        return Result.success(ssLQXXHZService.findAllssfx(zymc));
    }

    /****
     * 查找专项计划
     * @return
     */
    @GetMapping("/zxjh")
    @ApiOperation(value = "专项计划", notes = "查找所有专项计划")
    public Result findAllxxfs() {
        return Result.success(ssLQXXHZService.findAllZxjh());
    }

    /*****
     * 调档函打印
     * @param ksxm
     * @param response
     */
    @GetMapping("/outDiaoDangHan")
    @ApiOperation(value = "调档函", notes = "生成学生的调档函")
    public Result outDiaoDangHan(@RequestParam String ksbh,
                               @RequestParam String ksxm,
                               HttpServletResponse response){
        return ssLQXXHZService.outDiaoDangHan(ksbh, ksxm, response);
    }

    /******
     * 查找不同学院的学生总数
     * @param lqyxdm
     * @return
     */
    @GetMapping("/total/all")
    @ApiOperation(value = "查找不同学院的人数", notes = "查找不同学院的学生总数")
    public Result totalStudent(@RequestParam(defaultValue = "") String lqyxdm) {
        return Result.success(ssLQXXHZService.totalStudent(lqyxdm));
    }

    /******
     * 查找不同学院不同状态的学生
     * @param lqyxdm
     * @param shzt
     * @return
     */
    @GetMapping("/total/allSub")
    @ApiOperation(value = "不同状态的学生人数", notes = "查找不同学院不同状态的学生人数")
    public Result totalSutShzt(@RequestParam(defaultValue = "") String lqyxdm, @RequestParam(defaultValue = "") int shzt, @RequestParam(defaultValue = "0") int nf) {
        return Result.success(ssLQXXHZService.totalSutShzt(lqyxdm, shzt, nf));
    }

    @GetMapping("/total")
    @ApiOperation(value = "不同状态的学生人数", notes = "查找不同学院不同年份不同状态的学生")
    public Result total(@RequestParam(defaultValue = "") String lqyxdm, @RequestParam(defaultValue = "") String lqyxmc, @RequestParam(defaultValue = "0") int nf) {
        return Result.success(ssLQXXHZService.total(lqyxdm,lqyxmc,nf));
    }

    @GetMapping("/tongji")
    @ApiOperation(value = "不同学院不同状态的学生人数", notes = "查找不同学院各个状态的学生")
    public Result tongJi(@RequestParam(defaultValue = "0") int nf) {
        return Result.success(ssLQXXHZService.tongJi(nf));
    }

    @GetMapping("/xmm")
    @ApiOperation(value = "项目名称", notes = "查找所有专业学位项目名")
    public Result allXmm(){
        return Result.success(ssLQXXHZService.allXmm());
    }

    @GetMapping("/tgxmmc")
    @ApiOperation(value = "通过英语等级名称", notes = "查找英语等级名称")
    public Result allTgxmmc() {
        return Result.success(ssLQXXHZService.allTgxmmc());
    }

    @GetMapping("/nlqlb")
    @ApiOperation(value = "拟录取类别名", notes = "查找拟录取类别名称")
    public Result allNlqlb() {
        return Result.success(ssLQXXHZService.allNlqlb());
    }

    @GetMapping("/zxnf")
    @ApiOperation(value = "生成年份区间", notes = "生成年份区间")
    public Result zxnf() {
        return Result.success(ssLQXXHZService.zxnf());
    }
}
