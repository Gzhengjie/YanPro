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
import com.mingmen.yanpro.dao.Zyzsxx;
import com.mingmen.yanpro.service.ZyzsxxService;
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
@RequestMapping("/zyzsxx")
public class ZyzsxxController {

    @Autowired
    private ZyzsxxService zyzsxxService;

    //新增和修改
    @PostMapping("/save")
    public Map<String,Object> save(@RequestBody Zyzsxx zyzsxx){
        //新增或者更新

        //在新增数据的时候就设置好上传人上传时间
        zyzsxx.setSCR(TokenUtils.getCurrentUser().getXM());
        zyzsxx.setSCSJ(DateUtil.now());
        return zyzsxxService.saveZyzsxx(zyzsxx);
    }


    //查询所有
    @GetMapping("/selectAll")
    public List<Zyzsxx> selectAll(){
        return zyzsxxService.selectAll();
    }

    @GetMapping("/selectByYxsmc/{YXSMC}")
    public List<Zyzsxx> selectByYxsmc(@PathVariable String YXSMC){

        return zyzsxxService.selectByYxsmc(YXSMC);
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Map<String,Object> delete(@PathVariable Integer id){
        return zyzsxxService.deleteById(id);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        if (zyzsxxService.removeByIds(ids)) {
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
//        List<Zyzsxx> data = zyzsxxService.selectPage(pageNum,pageSize); // 分页查询
//        Integer total = zyzsxxService.selectTotal();
//        Map<String,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param SHZT
     * @param YXSMC
     * @param ZYMC
     * @return
     */
    @GetMapping("/page")
    public IPage<Zyzsxx> findPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "0") Integer NF,
                                  @RequestParam(defaultValue = "") Integer SHZT,
                                  @RequestParam(defaultValue = "") String YXSDM,
                                  @RequestParam(defaultValue = "") String YXSMC,
                                  @RequestParam(defaultValue = "") String ZYMC){
        IPage<Zyzsxx> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Fsfa> queryWrapper = new QueryWrapper<>();
        return zyzsxxService.findPage(new Page<>(pageNum,pageSize),NF,SHZT,YXSDM,YXSMC,ZYMC);
    }

    /**
     * 获取研究方向
     * @param ZYDM
     * @param NF
     * @return
     */
    @GetMapping("/allYJFX")
    public Result getYJFX( @RequestParam String ZYDM, @RequestParam Integer NF){
        return Result.success(zyzsxxService.getYJFX(ZYDM, NF));
    }

    /**
     * 搜素所有的院系所代码
     * * @param YXSDM
     * @return
     */
    @GetMapping("/YXSMC")
    public Result findallYXSMC1() {
        return Result.success(zyzsxxService.findallYXSMC1());
    }


    /**
     * 搜素对应院系所代码的院系所名称
     * * @param YXSDM
     * @return
     */
    @GetMapping("/allYXSMC")
    public Result findallYXSMC(@RequestParam String YXSDM) {
        return Result.success(zyzsxxService.findallYXSMC(YXSDM));
    }

    /**
     * 搜索所有专业名称
     * @param YXSMC
     * @return
     */
    @GetMapping("/allZYMC")
    public Result findallZYMC(@RequestParam String YXSMC,
                              @RequestParam(defaultValue = "0") int NF) {
        return Result.success(zyzsxxService.findallZYMC(YXSMC,NF));
    }

    /**
     * 搜索专业名称对应的专业代码
     * @param ZYMC
     * @return
     */
    @GetMapping("/allZYDM")
    public Result findallZYDM(@RequestParam String ZYMC,
                              @RequestParam(defaultValue = "0") int NF) {
        return Result.success(zyzsxxService.findallZYDM(ZYMC,NF));
    }

    /**
     * 搜索专业名称对应的学习方式
     * @param ZYMC
     * @return
     */
    @GetMapping("/allXXFS")
    public Result findallXXFS(@RequestParam String ZYMC,
                              @RequestParam(defaultValue = "0") int NF) {
        return Result.success(zyzsxxService.findallXXFS(ZYMC,NF));
    }


    /**
     * 搜素所有数据库中包含的年份
     * @return
     */
    @GetMapping("/allNF")
    public Result findallNF() {
        return Result.success(zyzsxxService.findallNF());
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

        Zyzsxx newZyzsxx = new Zyzsxx();
        newZyzsxx.setID(ID);
        newZyzsxx.setSHZT(SHZT);
        if (SHZT == 1) {
            newZyzsxx.setSCR(TokenUtils.getCurrentUser().getXM());
            newZyzsxx.setSCSJ(DateUtil.now());
        }
        else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            newZyzsxx.setYYSHR(TokenUtils.getCurrentUser().getXM());
            newZyzsxx.setYYSHSJ(DateUtil.now());
        } else {
            newZyzsxx.setXYSHR(TokenUtils.getCurrentUser().getXM());
            newZyzsxx.setXYSHSJ(DateUtil.now());
        }
        return Result.success(zyzsxxService.saveZyzsxx(newZyzsxx));

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
            Zyzsxx newZyzsxx = new Zyzsxx();
            newZyzsxx.setID(ID);
            newZyzsxx.setSHZT(SHZT);
            if (SHZT == 1) {
                newZyzsxx.setSCR(TokenUtils.getCurrentUser().getXM());
                newZyzsxx.setSCSJ(DateUtil.now());
            }
            else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
                newZyzsxx.setYYSHR(TokenUtils.getCurrentUser().getXM());
                newZyzsxx.setYYSHSJ(DateUtil.now());
            } else {
                newZyzsxx.setXYSHR(TokenUtils.getCurrentUser().getXM());
                newZyzsxx.setXYSHSJ(DateUtil.now());
            }
            zyzsxxService.saveZyzsxx(newZyzsxx);
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
//        List<Zyzsxx> list = zyzsxxService.selectAll();
        List<Zyzsxx> list = new ArrayList<>();
        for (Integer ID : IDs){
            Zyzsxx newZyzsxx = zyzsxxService.selectById(ID);
            list.add(newZyzsxx);
        }
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.addHeaderAlias("院系所代码", "院系所代码");
        writer.addHeaderAlias("院系所名称", "院系所名称");
        writer.addHeaderAlias("专业代码", "专业代码");
        writer.addHeaderAlias("专业名称", "专业名称");
        writer.addHeaderAlias("学习方式", "学习方式");
        writer.addHeaderAlias("研究方向", "研究方向");
        writer.addHeaderAlias("已接收推免生数", "已接收推免生数");
        writer.addHeaderAlias("最终公开招考总数", "最终公开招考总数");
        writer.addHeaderAlias("复试比例", "复试比例");
        writer.addHeaderAlias("是否需要调剂", "是否需要调剂");
        writer.addHeaderAlias("联系人及电话", "联系人及电话");
        writer.addHeaderAlias("调剂要求", "调剂要求");
        writer.addHeaderAlias("备注", "备注");
        writer.addHeaderAlias("年份", "年份");
        writer.addHeaderAlias("上传人", "上传人");
        writer.addHeaderAlias("上传时间", "上传时间");
        writer.addHeaderAlias("学院审核人", "学院审核人");
        writer.addHeaderAlias("学院审核时间", "学院审核时间");
        writer.addHeaderAlias("研院审核人", "研院审核人");
        writer.addHeaderAlias("研院审核时间", "研院审核时间");
        writer.addHeaderAlias("外国语是否审核", "外国语是否审核");
        writer.addHeaderAlias("更新时间", "更新时间");

        writer.setOnlyAlias(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 自动适应列宽
        writer.autoSizeColumnAll();

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("各专业招生信息表", "UTF-8");
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
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Zyzsxx> list = reader.readAll(Zyzsxx.class);
        zyzsxxService.saveBatch(list);
        return true;
    }

    /**
     * 各专业统计
     * @param NF
     * @return
     */
    @GetMapping("/tongji")
    public Result tongJi(@RequestParam(defaultValue = "0") int NF) {
        return Result.success(zyzsxxService.tongJi(NF));
    }

    @GetMapping("/getSFYXTJ")
    public String getSFYXTJ() {
        return zyzsxxService.getSFYXTJ();
    }

    /**
     * 上传数据库数据为exles
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
//                result = zyzsxxService.uploadExcl(file);
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
     * 下载数据库数据为exles
     *
     * @param response
     * @throws IOException
     */
//    @RequestMapping(value = "/ZyzsxxExcelDownloads", method = RequestMethod.GET)
//    public void downloadAllClassmate(HttpServletResponse response) throws IOException {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("各专业招生信息表");
//
//        List<Zyzsxx> zyzsxxList= zyzsxxService.selectAll();
//
//        String fileName = "各专业招生信息表"  + ".xls";//设置要导出的文件的名字
//        //新增数据行，并且设置单元格数据
//
//        int rowNum = 1;
//
//        String[] headers = { "院系所表.院系所代码（YXSDM）", "院系所表.院系所名称（YXSMC）", "硕士招生专业表.专业代码（ZYDM）", "硕士招生专业表.专业名称（ZYMC）", "学习方式（XXFS）", "计划总数（JHZS）", "已接收推免生数（YJSTMS）",
//                            "已接收推免生数（YJSTMS）", "最终公开招考总数（GKZKZS）", "复试比例（FSBL）", "是否需要调剂（SFXYTJ）", "联系人及电话（LXRJDH）",
//                            "调剂要求（DJYQ）", "备注（BZ）", "审核状态（SHZT）（0：学院秘书上传1：学院审核通过2：研究生院审核通过3：学院不通过4：研究生院不通过）", "年份（NF）", "上传人（SCR）",
//                            "上传时间（SCSJ）", "审核人（SHR）", "审核时间（SHSJ）", "更新时间（GXSJ）"};
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
//        for (Zyzsxx zyzsxx : zyzsxxList) {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            HSSFRow row1 = sheet.createRow(rowNum);
//            row1.createCell(0).setCellValue(zyzsxx.getYXSDM());
//            row1.createCell(1).setCellValue(zyzsxx.getYXSMC());
//            row1.createCell(2).setCellValue(zyzsxx.getZYDM());
//            row1.createCell(3).setCellValue(zyzsxx.getZYMC());
//            row1.createCell(4).setCellValue(zyzsxx.getXXFS());
//            row1.createCell(5).setCellValue(zyzsxx.getJHZS());
//            row1.createCell(6).setCellValue(zyzsxx.getYJSTMS());
//            row1.createCell(7).setCellValue(zyzsxx.getGKZKZS());
//            row1.createCell(8).setCellValue(zyzsxx.getFSBL());
//            row1.createCell(9).setCellValue(zyzsxx.getSFXYTJ());
//            row1.createCell(10).setCellValue(zyzsxx.getLXRJDH());
//            row1.createCell(11).setCellValue(zyzsxx.getDJYQ());
//            row1.createCell(12).setCellValue(zyzsxx.getBZ());
//            row1.createCell(13).setCellValue(zyzsxx.getSHZT());
//            row1.createCell(14).setCellValue(zyzsxx.getNF());
//            row1.createCell(15).setCellValue(zyzsxx.getSCR());
//            row1.createCell(16).setCellValue(formatter.format(zyzsxx.getSCSJ()));
//            row1.createCell(17).setCellValue(zyzsxx.getSHR());
//            row1.createCell(18).setCellValue(formatter.format(zyzsxx.getSHSJ()));
//            row1.createCell(19).setCellValue(formatter.format(zyzsxx.getGXSJ()));
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
