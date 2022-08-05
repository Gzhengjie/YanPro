package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;


@Data
@TableName(value = "ss_jianzhang_zykskm")
@ToString
public class SsJianzhangZykskm {
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
    @Alias("政治理论码")
    private String zzllm;
    @Alias("外国语码")
    private String wgym;
    @Alias("业务课1码")
    private String ywk1m;
    @Alias("业务课2码")
    private String ywk2m;
    @Alias("复试笔试科目")
    private String fsbskm;
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
    private String zzllmc;

    @TableField(exist = false)
    private String wgymc;

    @TableField(exist = false)
    private String ywk1mc;

    @TableField(exist = false)
    private String ywk2mc;

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
