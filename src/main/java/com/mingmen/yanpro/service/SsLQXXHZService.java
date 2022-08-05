package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.mingmen.yanpro.common.Constants;
import com.mingmen.yanpro.common.Result;
import com.mingmen.yanpro.dao.College;
import com.mingmen.yanpro.dao.SsLQXXHZ;
import com.mingmen.yanpro.dao.SszszyDao;
import com.mingmen.yanpro.dao.SszszyfxDao;
import com.mingmen.yanpro.mapper.SsLQXXHZMapper;
import com.mingmen.yanpro.mapper.SszszyMapper;
import com.mingmen.yanpro.mapper.SszszyfxMapper;
import com.mingmen.yanpro.utils.TokenUtils;
import com.mingmen.yanpro.mapper.CollegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SsLQXXHZService extends ServiceImpl<SsLQXXHZMapper, SsLQXXHZ> {

    @Autowired
    private SsLQXXHZMapper ssLQXXHZMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private ICollegeService collegeService;

    @Autowired
    private SszszyMapper sszszyMapper;

    @Autowired
    private SszszyfxMapper sszszyfxMapper;

    public Boolean saveUser(SsLQXXHZ ssLQXXHZ) {
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        ssLQXXHZ.setLqyxdm(TokenUtils.getCurrentUser().getYXSDM());
        queryWrapper.eq("yxsdm", TokenUtils.getCurrentUser().getYXSDM());
        ssLQXXHZ.setLqzydm(ssLQXXHZMapper.findLqzydm(ssLQXXHZ.getLqzymc()));
        ssLQXXHZ.setLqyxmc(collegeMapper.selectOne(queryWrapper).getYxsmc());
        if (ssLQXXHZ.getTgxmmc() != null)
            ssLQXXHZ.setTgxmdm(ssLQXXHZMapper.findtgxmdm(ssLQXXHZ.getTgxmmc()));
        if (ssLQXXHZ.getNlqlb() != null)
            ssLQXXHZ.setNlqlbm(ssLQXXHZMapper.findnlqlbm(ssLQXXHZ.getNlqlb()));
        if (ssLQXXHZ.getZxjhmc() != null)
            ssLQXXHZ.setZxjh(ssLQXXHZMapper.findzxjh(ssLQXXHZ.getZxjhmc()));
        if (ssLQXXHZ.getXmmc() != null)
            ssLQXXHZ.setXmm(ssLQXXHZMapper.findxxm(ssLQXXHZ.getXmmc()));
        return saveOrUpdate(ssLQXXHZ);
    }

    public Result add(SsLQXXHZ ssLQXXHZ) {
        if (!ssLQXXHZMapper.allow("硕士录取"))
            return Result.error(Constants.CODE_500, "不在提交时间范围");
        QueryWrapper<SsLQXXHZ> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("ksbh", ssLQXXHZ.getKsbh());
        if (ssLQXXHZMapper.selectCount(queryWrapper2) != 0) {
            return Result.error(Constants.CODE_500, "该学生已存在");
        }

        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        ssLQXXHZ.setScsj(DateUtil.today());
        ssLQXXHZ.setScr(TokenUtils.getCurrentUser().getXM());
        ssLQXXHZ.setLqyxdm(TokenUtils.getCurrentUser().getYXSDM());
        queryWrapper.eq("yxsdm", TokenUtils.getCurrentUser().getYXSDM());
        ssLQXXHZ.setLqzydm(ssLQXXHZMapper.findLqzydm(ssLQXXHZ.getLqzymc()));
        ssLQXXHZ.setLqyxmc(collegeMapper.selectOne(queryWrapper).getYxsmc());
        if (ssLQXXHZ.getTgxmmc() != null)
            ssLQXXHZ.setTgxmdm(ssLQXXHZMapper.findtgxmdm(ssLQXXHZ.getTgxmmc()));
        if (ssLQXXHZ.getNlqlb() != null)
            ssLQXXHZ.setNlqlbm(ssLQXXHZMapper.findnlqlbm(ssLQXXHZ.getNlqlb()));
        if (ssLQXXHZ.getZxjhmc() != null)
            ssLQXXHZ.setZxjh(ssLQXXHZMapper.findzxjh(ssLQXXHZ.getZxjhmc()));
        if (ssLQXXHZ.getXmmc() != null)
            ssLQXXHZ.setXmm(ssLQXXHZMapper.findxxm(ssLQXXHZ.getXmmc()));
        if (saveOrUpdate(ssLQXXHZ))
            return Result.success();
        else
            return Result.error(Constants.CODE_500, "保存失败");
    }

    public JSONArray findAlllqyxmc() {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssLQXXHZMapper.alllqyxmc();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findAlllqzymc(String lqyxdm, int nf) {
        if (nf == 0)
            nf = DateUtil.year(DateUtil.date());
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssLQXXHZMapper.alllqzymc(lqyxdm, nf);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public Result outDiaoDangHan(String ksbh, String ksxm, HttpServletResponse response) {
        QueryWrapper<SsLQXXHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("ksbh", ksbh);
        queryWrapper1.eq("ksxm", ksxm);
        SsLQXXHZ ssLQXXHZ = ssLQXXHZMapper.selectOne(queryWrapper1);
        if (ssLQXXHZ == null)
        {
            return Result.error(Constants.CODE_500,"该考生不存在");
        }
        if ( "否".equals(ssLQXXHZ.getSfdd())) {
            return Result.error(Constants.CODE_500,"该考生不需要调档");
        }
        if ( ssLQXXHZ.getShzt() != 4) {
            return Result.error(Constants.CODE_500,"该考生未审核完成");
        }
//        String xueYuan,String danWei,String zhuanYe,String luQuZhuanYe,
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("yxsmc", ssLQXXHZ.getLqyxmc());
        int numPage = Integer.parseInt(collegeService.getOne(queryWrapper).getYxsdm());

        try {
            String fileName ="调档函_"+ksxm+".pdf";
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","filename=" + new String(fileName.getBytes(), "iso8859-1"));
            String templatePath = "C:\\Users\\ASUS\\Desktop\\校企实训\\moban.pdf";//制作好的PDF模版路径
//            String templatePath = "/home/file/moban.pdf";//制作好的PDF模版路径
            PdfReader reader;
            Document document;
            FileOutputStream out = null;
            PdfReader pdfReader = null;
            ByteArrayOutputStream bos = null;
            PdfStamper stamper;
            List<PdfReader> list = new ArrayList();

            reader = new PdfReader(templatePath);

            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();//这个form就是pdf里面的表单key就是pdf的键 value 就是往里面要放的值
//            BaseFont baseFont = BaseFont.createFont("C:\\WINDOWS\\Fonts\\STSONG.TTF,1",BaseFont.IDENTITY_H,
//                    BaseFont.EMBEDDED);

//            BaseFont FSChinese = BaseFont.createFont("C:\\Windows\\Fonts\\STSONG.TTF",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);//这是设置仿宋基本字体
            BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font text=new Font(baseFont,12, Font.NORMAL);//这里设置字体大小和粗细
            form.addSubstitutionFont(baseFont);
            form.setFieldProperty("fill_1_"+numPage,"textsize",10f,null);
            form.setFieldProperty("fill_2_"+numPage,"textsize",14f,null);
            form.setFieldProperty("fill_3_"+numPage,"textsize",14f,null);
            form.setFieldProperty("fill_4_"+numPage,"textsize",14f,null);
            form.setFieldProperty("fill_6_"+numPage,"textsize",10f,null);
            form.setFieldProperty("undefined_"+numPage,"textsize",13f,null);
            form.setFieldProperty("fill_8_"+numPage,"textsize",13f,null);
            form.setFieldProperty("fill_10_"+numPage,"textsize",12f,null);
            form.setFieldProperty("fill_11_"+numPage,"textsize",12f,null);
            form.setField("fill_1_"+numPage, ksbh);
            form.setField("fill_2_"+numPage, ssLQXXHZ.getSzdw());
            form.setField("fill_3_"+numPage, ssLQXXHZ.getLqzymc());
            form.setField("fill_4_"+numPage, ksxm);
            form.setField("fill_6_"+numPage, ksbh);
            form.setField("undefined_"+numPage, ssLQXXHZ.getSzdw());
            form.setField("fill_8_"+numPage, ksxm);
            form.setField("fill_10_"+numPage, ksxm);
            form.setField("fill_11_"+numPage, ssLQXXHZ.getLqzymc());
//            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
//            String generationdate = dateformat.format(new Date());
//            form.setField("date", generationdate);

            //true代表生成的PDF文件不可编辑
            ServletOutputStream servletOutputStream = response.getOutputStream();
            stamper.setFormFlattening(true);
            stamper.close();
            pdfReader = new PdfReader(bos.toByteArray());
            list.add(pdfReader);
            Document doc = new Document(reader.getPageSize(1));
            PdfCopy copy = new PdfCopy(doc, response.getOutputStream());
            doc.open();
            for (int k = 0; k < list.size(); k++) {
                PdfReader pdfReader1 = list.get(k);
                PdfImportedPage page1 = copy.getImportedPage(pdfReader1, numPage);
                doc.newPage();
                copy.addPage(page1);
            }
            copy.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(true);
    }

    public IPage<SsLQXXHZ> findPage(Page<SsLQXXHZ> page, String lqyxdm, String lqyxmc, String lqzymc, String xxfs, String zxjh, String ksbh, String ksxm, int nf,int shzt) {

        if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            return ssLQXXHZMapper.findPage(page, lqyxdm, lqyxmc, lqzymc, xxfs, zxjh, ksbh, ksxm, nf, shzt);
        }
        else {
            return ssLQXXHZMapper.findPage(page, TokenUtils.getCurrentUser().getYXSDM(), lqyxmc, lqzymc, xxfs, zxjh, ksbh, ksxm, nf, shzt);
        }
    }

    public Result saveBatch(List<SsLQXXHZ> list, String yxsdm, String xm) {
        if (!ssLQXXHZMapper.allow("硕士录取"))
            return Result.error(Constants.CODE_500, "不在提交时间范围");
        List<SsLQXXHZ> err = new ArrayList<>();
        int i = 0;
        QueryWrapper<College> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("yxsdm", yxsdm);
        String yxsmc = collegeMapper.selectOne(queryWrapper3).getYxsmc();
        Pattern pattern = Pattern.compile("[0-9]*");

        for(SsLQXXHZ one : list) {
            int flag = 0;
            if (!pattern.matcher(one.getKsbh()+"").matches() || one.getKsbh().length() != 15){
                one.setBz(one.getBz() + "错误信息：考生编号位数错误或有非法字符");
                flag = 1;
            }
            one.setLqyxdm(yxsdm);
            one.setLqyxmc(yxsmc);
            QueryWrapper<SszszyDao> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("yxsdm", one.getLqyxdm());
            queryWrapper.eq("ZYDM", one.getLqzydm());
            queryWrapper.eq("ZYMC", one.getLqzymc());
            if (sszszyMapper.selectCount(queryWrapper) == 0){
                one.setBz(one.getBz()  + "错误信息：专业名称或专业代码错误");
                flag = 1;
            }
            QueryWrapper<SszszyfxDao> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("zydm", one.getLqzydm());
            queryWrapper1.eq("zymc", one.getLqzymc());
            queryWrapper1.eq("xxfs", one.getXxfs());
//            if (sszszyfxMapper.selectCount(queryWrapper1) == 0){
//                one.setBz(one.getBz() + "错误信息：学习方式错误");
//                err.add(one);
//                flag = 1;
//            }
            QueryWrapper<SsLQXXHZ> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("ksbh", one.getKsbh());
            if (ssLQXXHZMapper.selectCount(queryWrapper2) != 0)
            {
                one.setBz(one.getBz() + "错误信息：该考生已存在");
                flag = 1;
            }
            if (flag == 0){
                one.setScr(xm);
                one.setScsj(DateUtil.today());
                one.setTgxmdm(ssLQXXHZMapper.findtgxmdm(one.getTgxmmc()));
                one.setNlqlbm(ssLQXXHZMapper.findnlqlbm(one.getNlqlb()));
                one.setZxjh(ssLQXXHZMapper.findzxjh(one.getZxjhmc()));
                one.setXmm(ssLQXXHZMapper.findxxm(one.getXmmc()));
                saveOrUpdate(one);
                i++;
            }
            else {
                err.add(one);
            }
        }
        JSONObject jsonObject = new JSONObject();
        if (err.size() == 0)
            jsonObject.putOpt("all", true);
        else
            jsonObject.putOpt("all", false);
        jsonObject.putOpt("success", i);
        jsonObject.putOpt("error", err);
        jsonObject.putOpt("errorcount", err.size());
        return Result.success(jsonObject);
    }

    public JSONArray findAllssfx(@RequestParam String zymc) {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssLQXXHZMapper.allxxfs(zymc);
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray findAllZxjh() {
        JSONArray jsonArray = new JSONArray();
        List<HashMap<String, String>> allname = ssLQXXHZMapper.allZxjh();
        for(HashMap<String, String> name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name.get("zxjhmc"));
            jsonObject.putOpt("label", name.get("zxjhmc"));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public int totalStudent(String lqyxdm) {
        return ssLQXXHZMapper.totalStudent(lqyxdm);
    }

    public int totalSutShzt(String lqyxdm, int shzt, int nf) {
        return ssLQXXHZMapper.totalSutShzt(lqyxdm, "",shzt, nf);
    }

    public HashMap<String, Integer> total(String lqyxdm, String lqyxmc, int nf) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        if (nf == 0){
            nf = DateUtil.year(DateUtil.date());
        }
        String yxsdm = TokenUtils.getCurrentUser().getYXSDM();
        if ("1-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            hashMap.put("dshrs", ssLQXXHZMapper.totalSutShzt("", lqyxmc,2, nf));
            hashMap.put("shtgrs", ssLQXXHZMapper.totalSutShzt("", lqyxmc,4, nf));
            hashMap.put("shbtgrs", ssLQXXHZMapper.totalSutShzt("", lqyxmc,5, nf));
            return hashMap;
        }
        else if ("2-1".equals(TokenUtils.getCurrentUser().getYHZ())) {
            hashMap.put("dshrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",1, nf));
            hashMap.put("xyshtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",2, nf));
            hashMap.put("xyshbtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",3, nf));
            hashMap.put("yyshtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",4, nf));
            hashMap.put("yyshbtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",5, nf));
            return hashMap;
        }
        else {
            hashMap.put("dtj", ssLQXXHZMapper.totalSutShzt(yxsdm, "",0, nf));
            hashMap.put("ytj", ssLQXXHZMapper.totalSutShzt(yxsdm, "",1, nf));
            hashMap.put("xyshtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",2, nf));
            hashMap.put("xyshbtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",3, nf));
            hashMap.put("yyshtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",4, nf));
            hashMap.put("yyshbtgrs", ssLQXXHZMapper.totalSutShzt(yxsdm, "",5, nf));
            return hashMap;
        }
    }

    public JSONArray tongJi(int nf) {
        if (nf == 0){
            nf = DateUtil.year(DateUtil.date());
        }
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssLQXXHZMapper.alllqyxmc();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("xy", name);
            jsonObject.putOpt("dsh", ssLQXXHZMapper.totalSutShzt("", name, 2, nf));
            jsonObject.putOpt("yjsytg", ssLQXXHZMapper.totalSutShzt("", name, 4, nf));
            jsonObject.putOpt("yjsybtg", ssLQXXHZMapper.totalSutShzt("", name, 5, nf));
            jsonObject.putOpt("nf", nf);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public void export(String lqyxmc, int nf, int shzt, HttpServletResponse response) throws IOException {

        List<SsLQXXHZ> list = ssLQXXHZMapper.selectAll("", lqyxmc, "", nf, shzt);

        ExcelWriter writer = ExcelUtil.getWriter(true);

//        writer.writeHeadRow(rowData);
        //设置列宽（列,宽度）
        for(int i=0;i < list.size();i++){
            writer.setColumnWidth(i,15);
        }

        writer.addHeaderAlias("录取院系代码", "录取院系代码");
        writer.addHeaderAlias("录取院系名称", "录取院系名称");
        writer.addHeaderAlias("录取专业代码", "录取专业代码");
        writer.addHeaderAlias("录取专业名称", "录取专业名称");
        writer.addHeaderAlias("学习方式", "学习方式");
        writer.addHeaderAlias("考生姓名", "考生姓名");
        writer.addHeaderAlias("考生编号", "考生编号");
        writer.addHeaderAlias("导师姓名", "导师姓名");
        writer.addHeaderAlias("调剂标志", "调剂标志");
        writer.addHeaderAlias("初试总成绩", "初试总成绩");
        writer.addHeaderAlias("笔试成绩", "笔试成绩");
        writer.addHeaderAlias("面试成绩", "面试成绩");
        writer.addHeaderAlias("复试成绩", "复试成绩");
        writer.addHeaderAlias("总成绩", "总成绩");
        writer.addHeaderAlias("成绩排名", "成绩排名");
        writer.addHeaderAlias("四六级通过项目名称", "四六级通过项目名称");
        writer.addHeaderAlias("拟录取类别", "拟录取类别");
        writer.addHeaderAlias("专项计划计划码", "专项计划计划码");
        writer.addHeaderAlias("专业学位项目专项项目", "专业学位项目专项项目");
        writer.addHeaderAlias("定向就业单位", "定向就业单位");
        writer.addHeaderAlias("所在单位", "所在单位");
        writer.addHeaderAlias("是否调档", "是否调档");
        writer.addHeaderAlias("年份", "年份");
        writer.addHeaderAlias("备注", "备注");

        writer.setOnlyAlias(true);

        writer.write(list);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetel.sheet;charset=utf-8");
        String filemane = URLEncoder.encode("考生复试信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filemane + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    public JSONArray allXmm() {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssLQXXHZMapper.allXmm();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray allTgxmmc() {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssLQXXHZMapper.allTgxmmc();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray allNlqlb() {
        JSONArray jsonArray = new JSONArray();
        List<String> allname = ssLQXXHZMapper.allNlqlb();
        for(String name : allname){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("value", name);
            jsonObject.putOpt("label", name);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONObject zxnf() {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<SsLQXXHZ> queryWrapper = new QueryWrapper<>();
        jsonObject.putOpt("min", ssLQXXHZMapper.selectOne(queryWrapper.orderByDesc("nf").last("limit 1")).getNf());
        jsonObject.putOpt("max", DateUtil.year(DateUtil.date()));
        return jsonObject;
    }

}
