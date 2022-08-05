package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;


@Data
@TableName(value = "ss_jianzhang_zyds")
@ToString
public class SsJianzhangZyds {
    @TableId(type = IdType.AUTO)
    @Alias("自增ID（请忽略）")
    private Integer id;
    @Alias("院系所代码")
    private String yxsdm;
    @Alias("院系所名称")
    private String yxsmc;
    @Alias("专业代码")
    private String zydm;
    @Alias("专业名称")
    private String zymc;
    @Alias("研究方向代码")
    private String yjfxdm;
    @Alias("研究方向名称")
    private String yjfxmc;
    @Alias("指导教师组")
    private String zdjs;
    @Alias("年份")
    private String nf;
    @Alias("上传人")
    private String scr;
    @Alias("上传时间")
    private java.sql.Timestamp scsj;
    @Alias("学院审核人")
    private String xyshr;
    @Alias("学院审核时间")
    private java.sql.Timestamp xyshsj;
    @Alias("研院审核人")
    private String yyshr;
    @Alias("研院审核时间")
    private java.sql.Timestamp yyshsj;
    @Alias("更新时间")
    private java.sql.Timestamp gxsj;
    @Alias("备注")
    private String bz;

    @TableField(exist = false)
    private List<String> zdjslist;

    @TableField(exist = false)
    private String falseReason;


    public String getScsj() {
        if(this.scsj != null){
            return this.scsj.toString().substring(0, 19);
        } else {
            return null;
        }
    }

    public void setScsj(String scsj) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(scsj);
        } catch (Exception e) {
            e.printStackTrace();

        }
        this.scsj = ts;
    }

    public String getXyshsj() {
        ///System.out.println("aaa");
        if(this.xyshsj != null){
            return this.xyshsj.toString().substring(0, 19);
        } else {
            return null;
        }
    }

    public void setXyshsj(String xyshsj) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(xyshsj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.xyshsj = ts;
    }

    public String getYyshsj() {
        ///System.out.println("aaa");
        if(this.yyshsj != null){
            return this.yyshsj.toString().substring(0, 19);
        } else {
            return null;
        }
    }

    public void setYyshsj(String yyshsj) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(yyshsj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.yyshsj = ts;
    }

    public String getGxsj() {
        if(this.gxsj != null){
            return this.gxsj.toString().substring(0, 19);
        } else {
            return null;
        }
    }

    public void setGxsj(String gxsj) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(gxsj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.gxsj = ts;
    }
}
