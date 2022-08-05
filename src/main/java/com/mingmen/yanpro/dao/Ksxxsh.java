package com.mingmen.yanpro.dao;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "ss_kscjsh")
public class Ksxxsh {
//    @Alias("ID")
    private Integer ID;
    @Alias("考生编号")
    private Long KSBH;
    @Alias("姓名")
    private String XM;
    @Alias("证件号码")
    private String ZJHM;
    @Alias("电话")
    private Long DH;

    @Alias("政治理论码")
    private Integer ZZLLM;
    @Alias("政治理论名称")
    @TableField(exist = false)
    private String ZZLLMC;

    @Alias("外国语码")
    private Integer YGYM;
    @Alias("外国语名称")
    @TableField(exist = false)
    private String YGYMC;

    @Alias("业务课1码")
    private Integer YWK1M;
    @Alias("业务课1名称")
    @TableField(exist = false)
    private String YWK1MC;

    @Alias("业务课2码")
    private Integer YWK2M;
    @Alias("业务课2名称")
    @TableField(exist = false)
    private String YWK2MC;

    @Alias("政治理论是否审核")
    private Boolean ZZLLS;
    @Alias("外国语是否审核")
    private Boolean YGYS;
    @Alias("业务课1是否审核")
    private Boolean YWK1S;
    @Alias("业务课2是否审核")
    private Boolean YWK2S;
    @Alias("申请理由")
    private String  SQLY;
    @Alias("审核结论")
    private String  SHJL;
    @Alias("审核状态")
    private Integer SHZT;
    @Alias("政治理论成绩")
    private Integer ZZLLCJ;
    @Alias("外国语成绩")
    private Integer WGYCJ;
    @Alias("业务课1成绩")
    private Integer YWK1CJ;
    @Alias("业务课2成绩")
    private Integer YWK2CJ;
    @Alias("数据是否能够审核")
    private Boolean SJSFNGSH;
    @Alias("年份")
    private Integer NF;
    @Alias("上传人")
    private String SCR;
    @Alias("上传时间")
    private String SCSJ;
    @Alias("审核人")
    private String SHR;
    @Alias("审核时间")
    private String SHSJ;
    @Alias("更新时间")
    private String GXSJ;

    @TableField(exist = false)
    @Alias("备注")
    private String BZ;
}
