package com.mingmen.yanpro.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.*;
import com.mingmen.yanpro.service.BszsjzService;
import com.mingmen.yanpro.service.BszyzsrsService;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import static com.mingmen.yanpro.utils.CustomMergeStrategy.writetoExcel;

@RestController
@RequestMapping("/doctor/jz")
public class BszsjzController {

    @Autowired
    private BszsjzService bszsjzService;

    @Autowired
    private BszyzsrsService bszyzsrsService;

    //新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody BszsjzDao bszsjzDao) {
        //新增或更新
        if(bszsjzService.saveBszsjz(bszsjzDao)==0){
            Result result = new Result();
            result.setCode("401");
            result.setMsg("当前年份该专业下该研究方向已存在，无法新增");
            return result;
        }
        else {
            return Result.success(bszsjzService.saveBszsjz(bszsjzDao));
        }
    }

    //查询全部操作
    @GetMapping("/selectAll")
    public Result index() {
        return Result.success(bszsjzService.selectAll());
    }

    //删除操作，通过id进行删除
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        return Result.success(bszsjzService.deleteById(id));
    }

    //批量删除操作
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        Result result = new Result();
        for(Integer id:ids){
            int czm = bszsjzService.getNFCZ(id);
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
        return Result.success(bszsjzService.deleteBatch(ids));
    }

    //返回年份最小值
    @GetMapping("/minyear")
    public Result minYear() {
        UserDao userDao = TokenUtils.getCurrentUser();
        return Result.success(bszsjzService.minYear(userDao.getYXSDM()));
    }

    //查询当前材料是否允许提交，查询的是基础表
    @GetMapping("/getSFYXTJ")
    public Result getSFYXTJ() {
        return Result.success(bszsjzService.getSFYXTJ());
    }

    //返回单个学院的全部专业代码
    @GetMapping("/allzydm")
    public Result allZydm() {
        UserDao userDao = TokenUtils.getCurrentUser();
        return Result.success(bszsjzService.allZydm(userDao.getYXSDM()));
    }

    //返回单个学院的全部专业名称
    @GetMapping("/allzymc")
    public Result allZymc() {
        UserDao userDao = TokenUtils.getCurrentUser();
        return Result.success(bszsjzService.allZymc(userDao.getYXSDM()));
    }

    //用于研究生院
    @GetMapping("/allzymcByYXSDM")
    public Result allzymcByYXSDM(@RequestParam String yxsdm) {
        return Result.success(bszsjzService.allZymc(yxsdm));
    }

    //返回博士招生专业方向表中全部研究方向名称
    @GetMapping("/yjfxlist")
    public Result yjfxlist(@RequestParam String zymc) {
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        return Result.success(bszsjzService.yjfxlist(zymc,yxsdm));
    }

    //根据专业代码返回专业名称
    @GetMapping("/getZYMC")
    public Result getZYMC(@RequestParam String ZYDM) {
        return Result.success(bszsjzService.getZYMC(ZYDM));
    }

    //根据专业代码返回研究方向代码
    @GetMapping("/getYJFXDM")
    public Result getYJFXDM(@RequestParam String ZYDM) {
        return Result.success(bszsjzService.getYJFXDM(ZYDM));
    }

    //根据研究方向代码返回研究方向名称
    @GetMapping("/getYJFXMC")
    public Result getYJFXMC(@RequestParam String YJFXDM, @RequestParam String ZYDM) {
        return Result.success(bszsjzService.getYJFXMC(YJFXDM,ZYDM));
    }

    //根据院系所代码返回全部研究方向名称
    @GetMapping("/getAllYJFXMC")
    public Result getAllYJFXMC() {
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        return Result.success(bszsjzService.getAllYJFXMC(yxsdm));
    }

    //判断学院领导能否填写备注
    @GetMapping("/getxyldNFCZ")
    public Result getxyldNFCZ(@RequestParam Integer id) {
        Result result = new Result();
        int czm = bszsjzService.getxyldNFCZ(id);
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
        return Result.success();
    }

    //判断学院秘书能否编辑和删除
    @GetMapping("/getNFCZ")
    public Result getNFCZ(@RequestParam Integer id) {
        Result result = new Result();
        int czm = bszsjzService.getNFCZ(id);
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

    //判断学院秘书能否新增
    @GetMapping("/getNFXZ")
    public Result getNFXZ(@RequestParam String nf) {
        Result result = new Result();
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        int czm = bszsjzService.getNFXZ(yxsdm,nf);
        if(czm==1){
            result.setCode("401");
            result.setMsg("已上传，学院领导审核中");
            return result;
        }
        else if(czm==3){
            result.setCode("200");
            return result;
        }
        else if(czm==2){
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

    //返回某学院下某个专业的全部指导教师
    @GetMapping("/getAllZDJS")
    public Result getAllZDJS(@RequestParam String zydm) {
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        return Result.success(bszsjzService.getAllZDJS(zydm,yxsdm));
    }

    //判断博士简章表能否导入
    @GetMapping("/getNFSC")
    public Result getNFSC(@RequestParam String yxsdm, @RequestParam String nf) {
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

    //返回学院名称
    @GetMapping("/getAllYXSMC")
    public Result getAllYXSMC() {
        return Result.success(bszsjzService.getAllYXSMC());
    }

    /**
     * 分页查询，当年份为空时，默认显示当前年份的数据
     * @param pageNum
     * @param pageSize
     * @param yxsdm 院系所代码
     * @param yxsmc 院系所名称
     * @param yjfxdm 研究方向代码
     * @param yjfxmc 研究方向名称
     * @param zdjs 指导教师
     * @param kskm 考试科目
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
                           @RequestParam(defaultValue = "") String yjfxdm,
                           @RequestParam(defaultValue = "") String yjfxmc,
                           @RequestParam(defaultValue = "") String zdjs,
                           @RequestParam(defaultValue = "") String kskm,
                           @RequestParam(defaultValue = "") String nf){
        String yhz = TokenUtils.getCurrentUser().getYHZ();
        IPage<BszsjzDao> page = bszsjzService.findPage(new Page<>(pageNum, pageSize),yxsdm, yxsmc, zydm, zymc, yjfxdm, yjfxmc, zdjs, kskm, nf);
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
                       @RequestParam(defaultValue = "") String yjfxdm,
                       @RequestParam(defaultValue = "") String yjfxmc,
                       @RequestParam(defaultValue = "") String zdjs,
                       @RequestParam(defaultValue = "") String kskm,
                       @RequestParam(defaultValue = "") String nf) throws Exception {
        //从数据库查询出所有数据
        IPage<BszsjzDao> bszsjz = bszsjzService.findPage(new Page<>(pageNum, pageSize),yxsdm, yxsmc, zydm, zymc, yjfxdm, yjfxmc, zdjs, kskm, nf);
        List<BszsjzDao> list = bszsjz.getRecords();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("YXSDM", "院系所代码");
        writer.addHeaderAlias("YXSMC", "院系所名称");
        writer.addHeaderAlias("ZYDM", "专业代码");
        writer.addHeaderAlias("ZYMC", "专业名称");
        writer.addHeaderAlias("YJFXDM", "研究方向代码");
        writer.addHeaderAlias("YJFXMC", "研究方向名称");
        writer.addHeaderAlias("ZDJS", "指导教师");
        writer.addHeaderAlias("NZSRS", "拟招生人数");
        writer.addHeaderAlias("KSKM", "考核科目");
        writer.addHeaderAlias("LXDH", "联系电话");
        writer.addHeaderAlias("BZ", "备注");
        writer.addHeaderAlias("NF", "年份");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("博士研究方向表", "UTF-8");
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
        //List<GjzxjhDao> list = reader.readAll(GjzxjhDao.class);
        //方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<BszsjzDao> bszsjzDaos = CollUtil.newArrayList();
        for(List<Object> row:list) {
            BszsjzDao bszsjzDao = new BszsjzDao();
            bszsjzDao.setYXSDM(userFileReq.getYxsdm());
            bszsjzDao.setYXSMC(bszsjzService.getyxsdmandmc(userFileReq.getYxsdm()));
            bszsjzDao.setZYDM(row.get(0).toString());
            bszsjzDao.setZYMC(row.get(1).toString());
            bszsjzDao.setYJFXDM(row.get(2).toString());
            bszsjzDao.setYJFXMC(row.get(3).toString());
            bszsjzDao.setZDJS(row.get(4).toString().replaceAll("\n"," "));
            bszsjzDao.setKSKM(row.get(5).toString());
            bszsjzDao.setLXDH(row.get(6).toString());
            bszsjzDao.setNF(userFileReq.getNf());
            bszsjzDao.setSCR(userFileReq.getXm());
            bszsjzDaos.add(bszsjzDao);
        }

        List<BszsjzDao> saveornot = bszsjzService.saveBatch(bszsjzDaos);
        if(saveornot.size()>0){
            Integer del = bszsjzDaos.size() - saveornot.size();
            Result result = new Result();
            result.setCode("401");
            result.setMsg("应导入数据总计"+bszsjzDaos.size()+"条，实际导入"+ del + "条,错误信息已下载");
            result.setData(saveornot);
            return result;
        }else {
            return Result.success();
        }
    }

    //将研究方向代码和研究方向名称拼接起来
    @GetMapping("/yjfxdmpjyjfxmc")
    public Result yjfxdmpjyjfxmc(@RequestParam String yxsdm,@RequestParam String nf) {
        List<String> yjfxdmpjyjfxmc = bszsjzService.getyjfxdmandmc(yxsdm, nf);
        return Result.success(yjfxdmpjyjfxmc);
    }

    //将院系所代码和名称以及拟招生人数拼起来
    @GetMapping("/getyxsdmandmc")
    public Result getyxsdmandmc(@RequestParam String yxsdm,@RequestParam String nf) {
        String res = "";
        String yxsmc = bszsjzService.getyxsdmandmc(yxsdm);
        Integer rs = bszyzsrsService.getCollegeRs(yxsdm,nf);

        res = yxsdm + yxsmc + "(" + rs.toString() + "人）";
        return Result.success(res);
    }

    //将专业代码和专业名称拼接起来
    @GetMapping("/getzydmandmc")
    public Result getzydmandmc(@RequestParam String yxsdm, @RequestParam String nf){
        List<String> zydmpjzymc = bszyzsrsService.getzydmandmc(yxsdm, nf);
        return Result.success(zydmpjzymc);
    }

    //拼接简章
    @GetMapping("/pjJZBZ")
    public Result pjJZBZ(@RequestParam String yxsdm,@RequestParam String nf) {
        List<BsjianzhangDao> bsjianzhangDaos = bszsjzService.pjJZBZ(yxsdm,nf);
        String yxs = this.getyxsdmandmc(yxsdm,nf).getData().toString();
        Integer nzsrs = bszyzsrsService.getCollegeRs(yxsdm, nf);
        for(BsjianzhangDao bsjianzhangDao:bsjianzhangDaos){
            bsjianzhangDao.setYXSMC(yxs);
            bsjianzhangDao.setNZSRS(String.valueOf(nzsrs));
        }
        return Result.success(bsjianzhangDaos);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/loadmb")
    public void loadmb(HttpServletResponse response) throws Exception {
        //从数据库查询出所有数据
        IPage<BszsjzDao> bszsjz = bszsjzService.findPage(new Page<>(1,1),"", "", "", "", "", "", "", "", "");
        List<BszsjzDao> list = bszsjz.getRecords();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("YXSDM", "院系所代码");
        writer.addHeaderAlias("YXSMC", "院系所名称");
        writer.addHeaderAlias("ZYDM", "专业代码");
        writer.addHeaderAlias("ZYMC", "专业名称");
        writer.addHeaderAlias("YJFXDM", "研究方向代码");
        writer.addHeaderAlias("YJFXMC", "研究方向名称");
        writer.addHeaderAlias("ZDJS", "指导教师");
        writer.addHeaderAlias("NZSRS", "拟招生人数");
        writer.addHeaderAlias("KSKM", "考核科目");
        writer.addHeaderAlias("LXDH", "联系电话");
        writer.addHeaderAlias("NF", "年份");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.setOnlyAlias(true);
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("博士招生研究方向表导入模板", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //拼接简章下载
    @GetMapping("/pjJZBZ_download")
    public void pinJieDownload(@RequestParam String yxsdm, @RequestParam String nf, HttpServletResponse response) throws IOException {
        List<BsjianzhangDao> list = bszsjzService.pjJZBZ(yxsdm,nf);
        String yxs = this.getyxsdmandmc(yxsdm,nf).getData().toString();
        Integer nzsrs = bszyzsrsService.getCollegeRs(yxsdm, nf);
        for(BsjianzhangDao bsjianzhangDao:list){
            bsjianzhangDao.setYXSMC(yxs);
            bsjianzhangDao.setNZSRS(String.valueOf(nzsrs));
        }

        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(nf+"年"+bszsjzService.getyxsdmandmc(yxsdm)+"博士招生专业目录.xlsx", "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(writetoExcel(list));
        os.flush();
        os.close();
    }

}
