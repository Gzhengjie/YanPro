package com.mingmen.yanpro.dao;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BszzbfpDao {
    private Integer ID;
    private String YXSDM;
    //@JsonIgnore //忽略该字段，不给前端展示该字段
    private String YXSMC;
    private String ZSSL;
    private String ZSZL;
    private String TMSL;
    private String TMZL;
    private String NF;
    private String SCR;
    private Timestamp SCSJ;
    private Timestamp GXSJ;

    public String getSCSJ() {
        if(this.SCSJ != null){
            return this.SCSJ.toString().substring(0, 19);
        } else {
            return null;
        }
    }
    public void setSCSJ(String scsj) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(scsj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.SCSJ = ts;
    }

    public String getGXSJ() {
        if(this.GXSJ != null){
            return this.GXSJ.toString().substring(0, 19);
        } else {
            return null;
        }
    }

    public void setGXSJ(String gxsj) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(gxsj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.GXSJ = ts;
    }
}
