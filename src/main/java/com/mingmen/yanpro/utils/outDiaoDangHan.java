package com.mingmen.yanpro.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class outDiaoDangHan {

    public void outDiaoDangHan(String[] args) throws FileNotFoundException, DocumentException {

        //填充创建pdf
        PdfReader reader = null;
        PdfStamper stamp = null;

        try {
            reader = new PdfReader("C:\\Users\\ASUS\\Desktop\\模板.pdf");
            File deskFile = new File("C:\\Users\\ASUS\\Desktop\\test.pdf");
            stamp = new PdfStamper(reader, new FileOutputStream(deskFile));
            //取出报表模板中的所有字段
            AcroFields form = stamp.getAcroFields();
            //中文字体
            BaseFont baseFont = BaseFont.createFont("C:\\WINDOWS\\Fonts\\simsun.ttc,1",BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            form.addSubstitutionFont(baseFont);

            // 填充数据
            System.out.println(form);
            form.setField("name", "王晓博");
            //报告生成日期
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            String generationdate = dateformat.format(new Date());
            form.setField("date", generationdate);
            stamp.setFormFlattening(true);
            stamp.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stamp != null) {
//                stamp.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

}
