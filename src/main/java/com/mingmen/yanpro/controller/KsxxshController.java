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
import com.mingmen.yanpro.dao.Fsfa;
import com.mingmen.yanpro.dao.Ksxxsh;
import com.mingmen.yanpro.service.KsxxshService;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/ksxxsh")
public class KsxxshController {

    @Autowired
    private KsxxshService ksxxshService;

    //新增和修改
    @PostMapping("/save")
    public Map<String,Object> save(@RequestBody Ksxxsh ksxxsh){
        //新增或者更新

        //在新增数据的时候就设置好上传人上传时间
//        ksxxsh.setSCR(TokenUtils.getCurrentUser().getXM());
//        ksxxsh.setSCSJ(DateUtil.now());

        return ksxxshService.saveKsxxsh(ksxxsh);
    }

    //查询所有
    @GetMapping("/selectAll")
    public List<Ksxxsh> selectAll(){
        return ksxxshService.selectAll();
    }

    /**
     * 学生申请复核界面
     * @param KSBH
     * @param XM
     * @param ZJHM
     * @return
     */
    @GetMapping("/selectOne")
    public Result selectByKsbh(@RequestParam(defaultValue = "") Long KSBH,
                                     @RequestParam(defaultValue = "") String XM,
                                     @RequestParam(defaultValue = "") String ZJHM){
        return Result.success(ksxxshService.selectOne(KSBH,XM,ZJHM));
    }



    @GetMapping("/selectModified")
    public List<Ksxxsh> selectModified(){
        return ksxxshService.selectModified();
    }

    @GetMapping("/selectModifiedByKsbh/{KSBH}")
    public List<Ksxxsh> selectModified(@PathVariable Long KSBH){

        return ksxxshService.selectModifiedByKsbh(KSBH);
    }

    /**
     * 搜素所有数据库中包含的年份
     * @return
     */
    @GetMapping("/allNF")
    public Result findallNF() {
        return Result.success(ksxxshService.findallNF());
    }

    /**
     * 搜素所有数据库中包含的考试科目代码
     * @return
     */
    @GetMapping("/allKSKMDM")
    public Result findallKSKMDM() {
        return Result.success(ksxxshService.findallKSKMDM());
    }

    //删除
    @DeleteMapping("/delete/{KSBH}")
    public Map<String,Object> delete(@PathVariable Long KSBH){
        return ksxxshService.deleteByKsbh(KSBH);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        if (ksxxshService.removeByIds(ids)) {
            return Result.success();
        }
        else {
            return Result.error(Constants.CODE_500, "系统错误");
        }
    }

    /**
     * 分页查询
     * 接口路径：/user/page?pageNum=1&pageSize=5
     * @RequestParam 接收参数 pageNum=1&pageSize=5
     * limit 第一个参数 = （pageNum-1）*pageSize
     *       第二个参数 pageSize
     */
//    @GetMapping("/findPage")
//    public Map<String,Object> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
//        pageNum = (pageNum - 1) * pageSize;
//        List<Ksxxsh> data = ksxxshService.selectPage(pageNum,pageSize); // 分页查询
//        Integer total = ksxxshService.selectTotal();
//        Map<String,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param NF
     * @param SHZT
     * @param KSBH
     * @param XM
     * @param ZJHM
     * @return
     */
    @GetMapping("/page")
    public IPage<Ksxxsh> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") Integer NF,
                                @RequestParam(defaultValue = "") Integer SHZT,
                                @RequestParam(defaultValue = "") Long KSBH,
                                @RequestParam(defaultValue = "") String XM,
                                @RequestParam(defaultValue = "") String ZJHM){
        IPage<Ksxxsh> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Fsfa> queryWrapper = new QueryWrapper<>();
        return ksxxshService.findPage(new Page<>(pageNum,pageSize),NF,SHZT,KSBH,XM,ZJHM);
    }

    /**
     * 学生申请复核
     * @param ID
     * @param SHZT
     * @param ZZLLS
     * @param YGYS
     * @param YWK1S
     * @param YWK2S
     * @param SQLY
     * @param DH
     * @return
     */
    @RequestMapping("/fh")
    public Map<String,Object> fh(@RequestParam Integer ID,
                       @RequestParam Integer SHZT,
                       @RequestParam Boolean ZZLLS,
                       @RequestParam Boolean YGYS,
                       @RequestParam Boolean YWK1S,
                       @RequestParam Boolean YWK2S,
                       @RequestParam String SQLY,
                       @RequestParam(defaultValue = "") Long DH,
                       @RequestParam(defaultValue = "") String XM
                       ) {  //输入ID、审核状态
        Ksxxsh newKsxxsh = new Ksxxsh();
        newKsxxsh.setID(ID);
        newKsxxsh.setZZLLS(ZZLLS);
        newKsxxsh.setYGYS(YGYS);
        newKsxxsh.setYWK1S(YWK1S);
        newKsxxsh.setYWK2S(YWK2S);
        newKsxxsh.setSHZT(SHZT);
        newKsxxsh.setSQLY(SQLY);
        newKsxxsh.setDH(DH);
        newKsxxsh.setSCR(XM);
        newKsxxsh.setSCSJ(DateUtil.now());
        return ksxxshService.saveKsxxsh(newKsxxsh);
    }

    /**
     * 单个审核
     * @param ID
     * @param SHZT
     * @return
     */
    @RequestMapping("/sh")  //单个审核
    public Result xxsh(@RequestParam Integer ID,
                        @RequestParam Integer SHZT) {  //输入ID、审核状态


        Ksxxsh newKsxxsh = new Ksxxsh();
        newKsxxsh.setID(ID);
        newKsxxsh.setSHZT(SHZT);

//        if (SHZT == 2) {
//            newKsxxsh.setSCR(TokenUtils.getCurrentUser().getXM());
//            newKsxxsh.setSCSJ(DateUtil.now());
//        }
        if (SHZT == 3) {
            newKsxxsh.setSHR(TokenUtils.getCurrentUser().getXM());
            newKsxxsh.setSHSJ(DateUtil.now());
        }

        return Result.success(ksxxshService.saveKsxxsh(newKsxxsh));
    }

    /**
     * 批量审核
     * @param IDs
     * @param SHZT
     * @return
     */
    @PostMapping("/sh/batch")
    public Result xxbatchsh(@RequestParam List<Integer> IDs, @RequestParam Integer SHZT) {
        for(Integer ID : IDs){

            Ksxxsh newKsxxsh = new Ksxxsh();
            newKsxxsh.setID(ID);
            newKsxxsh.setSHZT(SHZT);

//            if (SHZT == 2) {
//                newKsxxsh.setSCR(TokenUtils.getCurrentUser().getXM());
//                newKsxxsh.setSCSJ(DateUtil.now());
//            }

            if (SHZT == 3) {
                newKsxxsh.setSHR(TokenUtils.getCurrentUser().getXM());
                newKsxxsh.setSHSJ(DateUtil.now());
            }
            ksxxshService.saveKsxxsh(newKsxxsh);
        }
        return Result.success(true);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam List<Integer> IDs) throws Exception {
        // 从数据库查询出所有的数据
       List<Ksxxsh> list = new ArrayList<>();
        for (Integer ID : IDs){
            Ksxxsh newKsxxsh = ksxxshService.selectById(ID);
            list.add(newKsxxsh);
        }
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //        writer.writeHeadRow(rowData);
        //设置列宽（列,宽度）
        for(int i=0;i < list.size();i++){
            writer.setColumnWidth(i,25);
        }

        writer.addHeaderAlias("考生编号", "考生编号");
        writer.addHeaderAlias("姓名", "姓名");
        writer.addHeaderAlias("证件号码", "证件号码");
        writer.addHeaderAlias("电话", "电话");
        writer.addHeaderAlias("政治理论码", "政治理论码");
        writer.addHeaderAlias("政治理论名称", "政治理论名称");
        writer.addHeaderAlias("外国语码", "外国语码");
        writer.addHeaderAlias("外国语名称", "外国语名称");
        writer.addHeaderAlias("业务课1码", "业务课1码");
        writer.addHeaderAlias("业务课1名称", "业务课1名称");
        writer.addHeaderAlias("业务课2码", "业务课2码");
        writer.addHeaderAlias("业务课2名称", "业务课2名称");
        writer.addHeaderAlias("申请理由", "申请理由");
        writer.addHeaderAlias("审核结论", "审核结论");
        writer.addHeaderAlias("政治理论成绩", "政治理论成绩");
        writer.addHeaderAlias("外国语成绩", "外国语成绩");
        writer.addHeaderAlias("业务课1成绩", "业务课1成绩");
        writer.addHeaderAlias("业务课2成绩", "业务课2成绩");
        writer.addHeaderAlias("政治理论是否审核", "政治理论是否审核");
        writer.addHeaderAlias("外国语是否审核", "外国语是否审核");
        writer.addHeaderAlias("业务课1是否审核", "业务课1是否审核");
        writer.addHeaderAlias("业务课2是否审核", "业务课2是否审核");
        writer.addHeaderAlias("年份", "年份");
        writer.addHeaderAlias("上传人", "上传人");
        writer.addHeaderAlias("上传时间", "上传时间");
        writer.addHeaderAlias("审核人", "审核人");
        writer.addHeaderAlias("审核时间", "审核时间");
        writer.addHeaderAlias("更新时间", "更新时间");

        writer.setOnlyAlias(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 自动适应列宽
        writer.autoSizeColumnAll();

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("考生信息审核表", "UTF-8");
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
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Ksxxsh> list = reader.readAll(Ksxxsh.class);
        for (int i = 0;i<list.size();i++){
            list.get(i).setSHZT(0);
        }
        return ksxxshService.saveBatch(list);

//        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<Ksxxsh> ksxxshs = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            Ksxxsh ksxxsh = new Ksxxsh();
//            ksxxsh.setKSBH(Long.valueOf(row.get(0).toString()));
//            ksxxsh.setXM(row.get(1).toString());
//            ksxxsh.setZJHM(row.get(2).toString());
//            ksxxsh.setDH(Long.valueOf(row.get(3).toString()));
//            ksxxsh.setZZLLM(Integer.valueOf(row.get(4).toString()));
//            ksxxsh.setYGYM(Integer.valueOf(row.get(5).toString()));
//            ksxxsh.setYWK1M(Integer.valueOf(row.get(6).toString()));
//            ksxxsh.setYWK2M(Integer.valueOf(row.get(7).toString()));
//            ksxxsh.setZZLLCJ(Integer.valueOf(row.get(8).toString()));
//            ksxxsh.setWGYCJ(Integer.valueOf(row.get(9).toString()));
//            ksxxsh.setYWK1CJ(Integer.valueOf(row.get(10).toString()));
//            ksxxsh.setYWK2CJ(Integer.valueOf(row.get(11).toString()));
//
//            ksxxshs.add(ksxxsh);
//        }
//        ksxxshService.saveBatch(ksxxshs);
    }

    @GetMapping("/getSFYXTJ")
    public String getSFYXTJ() {
        return ksxxshService.getSFYXTJ();
    }

    /**
     * 上传数据库数据为exles 导出
     *
     * @param response
     * @throws IOException
     */
    // 实现excel文件上传并且导入进数据库中
//    @RequestMapping(value = "/uploadExcl")
//    public @ResponseBody
//    Map<String ,Object> uploadExcl(HttpServletRequest request, @RequestParam("file") MultipartFile file){
//        Map<String ,Object> result = new HashMap<>();
//        String path = request.getSession().getServletContext().getRealPath("/");
//        try{
//            // 如果文件不为空，写入上传路径
//            if(!file.isEmpty()){
//                result = ksxxshService.uploadExcl(file);
//            }else {
//                result.put("code","1");
//                result.put("message","上传文件为空！");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        if (result.get("code").equals("0")){
//            //根据时间戳创建新的文件名，这样即便是第二次上传相同名称的文件，也不会把第一次的文件覆盖了
//            //也可以用UUID创建
//            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
//            //通过req.getServletContext().getRealPath("") 获取当前项目的真实路径，然后拼接前面的文件名
//            String destFileName = request.getContextPath()+ "uploaded" + File.separator + fileName;
//            System.out.println(request.getServletPath());
//            System.out.println(request.getServletContext());
//            System.out.println(request.getServletContext().getRealPath(""));
//            System.out.println(request.getServletContext().getRealPath("/"));
//            System.out.println(request.getContextPath());
//            System.out.println(destFileName);
//            //第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录
//            File destFile = new File(destFileName);
//            destFile.getParentFile().mkdirs();
//            System.out.println(destFile);
//            //把浏览器上传的文件复制到希望的位置
//            try {
//                file.transferTo(destFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(fileName);
//        }
//        return result;
//    }

    /**
     * 下载数据库数据为exles 导出
     *
     * @param response
     * @throws IOException
     */
//    @RequestMapping(value = "/KsxxshExcelDownloads", method = RequestMethod.GET)
//    public void downloadAllClassmate(HttpServletResponse response) throws IOException {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("考生信息审核表");
//
//        List<Ksxxsh> ksxxshList= ksxxshService.selectAll();
//
//        String fileName = "考生信息审核表"  + ".xls";//设置要导出的文件的名字
//        //新增数据行，并且设置单元格数据
//
//        int rowNum = 1;
//
//        String[] headers = { "考生编号（KSBH）", "姓名（XM）", "证件号码（ZJHM）", "电话（DH）", "硕士考试科目表.政治理论码（ZZLLM）", "硕士考试科目表.外国语码（YGYM）", "硕士考试科目表.业务课1码（YWK1M）",
//                            "硕士考试科目表.业务课2码（YWK2M）", "政治理论是否审核（ZZLLS）", "外国语是否审核（YGYS）", "业务课1是否审核（YWK1S）", "业务课2是否审核（YWK2S）",
//                            "申请理由（SQLY）", "审核结论（SHJL）", "审核状态（SHZT）（0：无操作1：未批准2：复核中3：复核完成）", "政治理论成绩（ZZLLCJ）", "外国语成绩（WGYCJ）",
//                            "业务课1成绩（YWK1CJ）", "业务课2成绩（YWK2CJ）", "数据是否能够审核（SJSFNGSH）", "年份（NF）", "上传人（SCR）", "上传时间（SCSJ）", "审核人（SHR）", "审核时间（SHSJ）", "更新时间（GXSJ）"};
//        //headers表示excel表中第一行的表头
//
//        HSSFRow row = sheet.createRow(0);
//        //在excel表中添加表头
//
//        for(int i=0;i<headers.length;i++){
//            HSSFCell cell = row.createCell(i);
//            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
//            cell.setCellValue(text);
//        }
//
//        //在表中存放查询到的数据放入对应的列
//        for (Ksxxsh ksxxsh : ksxxshList) {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            HSSFRow row1 = sheet.createRow(rowNum);
//            row1.createCell(0).setCellValue(ksxxsh.getKSBH());
//            row1.createCell(1).setCellValue(ksxxsh.getXM());
//            row1.createCell(2).setCellValue(ksxxsh.getZJHM());
//            row1.createCell(3).setCellValue(ksxxsh.getDH());
//            row1.createCell(4).setCellValue(ksxxsh.getZZLLM());
//            row1.createCell(5).setCellValue(ksxxsh.getYGYM());
//            row1.createCell(6).setCellValue(ksxxsh.getYWK1M());
//            row1.createCell(7).setCellValue(ksxxsh.getYWK2M());
//            row1.createCell(8).setCellValue(ksxxsh.getZZLLS());
//            row1.createCell(9).setCellValue(ksxxsh.getYGYS());
//            row1.createCell(10).setCellValue(ksxxsh.getYWK1S());
//            row1.createCell(11).setCellValue(ksxxsh.getYWK2S());
//            row1.createCell(12).setCellValue(ksxxsh.getSQLY());
//            row1.createCell(13).setCellValue(ksxxsh.getSHJL());
//            row1.createCell(14).setCellValue(ksxxsh.getSHZT());
//            row1.createCell(15).setCellValue(ksxxsh.getZZLLCJ());
//            row1.createCell(16).setCellValue(ksxxsh.getWGYCJ());
//            row1.createCell(17).setCellValue(ksxxsh.getYWK1CJ());
//            row1.createCell(18).setCellValue(ksxxsh.getYWK2CJ());
//            row1.createCell(19).setCellValue(ksxxsh.getSJSFNGSH());
//            row1.createCell(20).setCellValue(ksxxsh.getNF());
//            row1.createCell(21).setCellValue(ksxxsh.getSCR());
//            row1.createCell(22).setCellValue(formatter.format(ksxxsh.getSCSJ()));
//            row1.createCell(23).setCellValue(ksxxsh.getSHR());
//            row1.createCell(24).setCellValue(formatter.format(ksxxsh.getSHSJ()));
//            row1.createCell(25).setCellValue(formatter.format(ksxxsh.getGXSJ()));
////            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            String dateString = formatter.format(user.getCreateTime());
////            row1.createCell(3).setCellValue(dateString);
////            System.out.println(dateString);
//            rowNum++;
//        }
//
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
//        response.flushBuffer();
//        workbook.write(response.getOutputStream());
//    }

}
