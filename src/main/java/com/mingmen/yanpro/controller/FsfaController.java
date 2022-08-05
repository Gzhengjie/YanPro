package com.mingmen.yanpro.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.Fsfa;
import com.mingmen.yanpro.service.FsfaService;
import com.mingmen.yanpro.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Colin
 * @date 2022/4/28 23:27
 */

@RestController
@RequestMapping("/fsfa")
public class FsfaController {

    @Autowired
    private FsfaService fsfaService;

    //新增和修改
    @PostMapping("/save")
    public Map<String,Object> save(@RequestBody Fsfa fsfa){
        //新增或者更新
        fsfa.setSCR(TokenUtils.getCurrentUser().getXM());
        fsfa.setSCSJ(DateUtil.now());
        return fsfaService.saveFsfa(fsfa);
    }

    //查询所有
    @GetMapping("/selectAll")
    public List<Fsfa> selectAll(){
        return fsfaService.selectAll();
    }

    //根据文件名查询
    @GetMapping("/selectByWjm/{WJM}")
    public List<Fsfa> selectByWjm(@PathVariable String WJM){
        return fsfaService.selectByWjm(WJM);
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Map<String,Object> delete(@PathVariable Integer id){
        return fsfaService.deleteById(id);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        if (fsfaService.removeByIds(ids)) {
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
//        List<Fsfa> data = fsfaService.selectPage(pageNum,pageSize); // 分页查询
//        Integer total = fsfaService.selectTotal();
//        Map<String,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }

    /**
     * 搜素所有数据库中包含的年份
     * @return
     */
    @GetMapping("/allNF")
    public Result findallNF() {
        return Result.success(fsfaService.findallNF());
    }

    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @param NF
     * @param SHZT
     * @param WJM
     * @param YXSDM
     * @param YXSMC
     * @return
     */
    @GetMapping("/page")
    public IPage<Fsfa> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "0") Integer NF,
                                @RequestParam(defaultValue = "") Integer SHZT,
                                @RequestParam(defaultValue = "") String WJM,
                                @RequestParam(defaultValue = "") String YXSDM,
                                @RequestParam(defaultValue = "") String YXSMC){
        IPage<Fsfa> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Fsfa> queryWrapper = new QueryWrapper<>();
        return fsfaService.findPage(new Page<>(pageNum,pageSize),NF,SHZT,WJM,YXSDM,YXSMC);
    }

    /**
     * 搜素所有的院系所代码
     * * @param YXSDM
     * @return
     */
    @GetMapping("/YXSMC")
    public Result findallYXSMC1() {
        return Result.success(fsfaService.findallYXSMC1());
    }

    @GetMapping("/allYXSMC")
    public Result findallYXSMC(@RequestParam String YXSDM) {
        return Result.success(fsfaService.findallYXSMC(YXSDM));
    }


    /**
     * 选中导出
     * @param response
     * @param IDs
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam List<Integer> IDs) throws Exception {
        // 从数据库查询出所有的数据
//        List<Fsfa> list = fsfaService.selectAll();
        List<Fsfa> list = new ArrayList<>();
        for (Integer ID : IDs){
            Fsfa newFsfa = fsfaService.selectById(ID);
            list.add(newFsfa);
        }
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.addHeaderAlias("文件名", "文件名");
        writer.addHeaderAlias("院系代码", "院系代码");
        writer.addHeaderAlias("院系所名称", "院系所名称");
        writer.addHeaderAlias("年份", "年份");
        writer.addHeaderAlias("上传人", "上传人");
        writer.addHeaderAlias("上传时间", "上传时间");
        writer.addHeaderAlias("学院审核人", "学院审核人");
        writer.addHeaderAlias("学院审核时间", "学院审核时间");
        writer.addHeaderAlias("研院审核人", "研院审核人");
        writer.addHeaderAlias("研院审核时间", "研院审核时间");
        writer.addHeaderAlias("更新时间", "更新时间");

        writer.setOnlyAlias(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 自动适应列宽
        writer.autoSizeColumnAll();

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("复试方案表", "UTF-8");
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
        List<Fsfa> list = reader.readAll(Fsfa.class);
        fsfaService.saveBatch(list);
        return true;
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
        Fsfa newFsfa = new Fsfa();
        newFsfa.setID(ID);
        newFsfa.setSHZT(SHZT);
        if (SHZT == 1) {
            newFsfa.setSCR(TokenUtils.getCurrentUser().getXM());
            newFsfa.setSCSJ(DateUtil.now());
        }
        else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            newFsfa.setYYSHR(TokenUtils.getCurrentUser().getXM());
            newFsfa.setYYSHSJ(DateUtil.now());
        } else {
            newFsfa.setXYSHR(TokenUtils.getCurrentUser().getXM());
            newFsfa.setXYSHSJ(DateUtil.now());
        }
        return Result.success(fsfaService.saveFsfa(newFsfa));
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
            Fsfa newFsfa = new Fsfa();
            newFsfa.setID(ID);
            newFsfa.setSHZT(SHZT);
            if (SHZT == 1) {
                newFsfa.setSCR(TokenUtils.getCurrentUser().getXM());
                newFsfa.setSCSJ(DateUtil.now());
            }
            else if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
                newFsfa.setYYSHR(TokenUtils.getCurrentUser().getXM());
                newFsfa.setYYSHSJ(DateUtil.now());
            } else {
                newFsfa.setXYSHR(TokenUtils.getCurrentUser().getXM());
                newFsfa.setXYSHSJ(DateUtil.now());
            }
            fsfaService.saveFsfa(newFsfa);
        }
        return Result.success(true);
    }

    /**
     * 各专业统计
     * @param NF
     * @return
     */
    @GetMapping("/tongji")
    public Result tongJi(@RequestParam(defaultValue = "0") int NF) {
        return Result.success(fsfaService.tongJi(NF));
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file) {    //注意参数
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
//            log.info("上传的文件名为：" + fileName);//写日志
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
//            log.info("文件的后缀名为：" + suffixName);//写日志
            // 设置文件存储路径         *************************************************
            String filePath = "D:/Java/files/";
            String path = filePath + fileName;
            File dest = new File(new File(path).getAbsolutePath());// dist为文件，有多级目录的文件
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    /**
     * 下载文件
     * @param WJM
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/download/{WJM}")
    public Integer downloadFile(@PathVariable String WJM,HttpServletRequest request, HttpServletResponse response) {
        String fileName = WJM + StrUtil.DOT + "doc";// 文件名
        String filePath = "D:/Java/files/";
        System.out.println(fileName);
        if (fileName != null) {
            //设置文件路径
            File file = new File(filePath,fileName);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally { // 做关闭操作
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return 0;
    }

    @GetMapping("/getSFYXTJ")
    public String getSFYXTJ() {
        return fsfaService.getSFYXTJ();
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
//                result = fsfaService.uploadExcl(file);
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
//    @RequestMapping(value = "/FsfaExcelDownloads", method = RequestMethod.GET)
//    public void downloadAllClassmate(HttpServletResponse response) throws IOException {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("复试方案表");
//
//        List<Fsfa> fsfaList= fsfaService.selectAll();
//
//        String fileName = "复试方案表"  + ".xls";//设置要导出的文件的名字
//        //新增数据行，并且设置单元格数据
//
//        int rowNum = 1;
//
//        String[] headers = { "文件名（WJM）", "院系所表.院系所代码（YXDM）", "院系所表.院系所名称（YXSMC）", "年份（NF）", "复试方案（FSFA）", "审核状态（SHZT）（0：学院秘书上传1：学院审核通过2：研究生院审核通过3：学院不通过4：研究生院不通过）", "上传人（SCR）",
//                "上传时间（SCSJ）", "审核人（SHR）", "审核时间（SHSJ）", "更新时间（GXSJ）"};
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
//        for (Fsfa fsfa : fsfaList) {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            HSSFRow row1 = sheet.createRow(rowNum);
//            row1.createCell(0).setCellValue(fsfa.getWJM());
//            row1.createCell(1).setCellValue(fsfa.getYXDM());
//            row1.createCell(2).setCellValue(fsfa.getYXSMC());
//            row1.createCell(3).setCellValue(fsfa.getNF());
//            row1.createCell(4).setCellValue(fsfa.getFSFA());
//            row1.createCell(5).setCellValue(fsfa.getSHZT());
//            row1.createCell(6).setCellValue(fsfa.getSCR());
//            row1.createCell(7).setCellValue(formatter.format(fsfa.getSCSJ()));
//            row1.createCell(8).setCellValue(fsfa.getSHR());
//            row1.createCell(9).setCellValue(formatter.format(fsfa.getSHSJ()));
//            row1.createCell(10).setCellValue(formatter.format(fsfa.getGXSJ()));
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
