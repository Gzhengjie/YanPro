package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("ss_jianzhang_zyxwgg")
public class SsJianZhangZyxwgg {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String yxsdm;
    private String ysxmc;
    private String wjmc;
    private String type;
    private Long size;
    private String url;
    private String md5;
    private String nf;
    private String scr;
    private java.sql.Timestamp scsj;
    private String xyshr;
    private java.sql.Timestamp xyshsj;
    private String yyshr;
    private java.sql.Timestamp yyshsj;
    private java.sql.Timestamp gxsj;
    private String bz;

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
