package com.mingmen.yanpro.dao;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Colin
 */

@Data
@TableName(value = "ss_fushi_fangan")
public class Fsfa {
    @Alias("ID")
    private Integer ID;
    @Alias("文件名")
    private String WJM;
    @Alias("院系代码")
    private String YXDM;
    @Alias("院系所名称")
    private String YXSMC;
    @Alias("年份")
    private Integer NF;
    @Alias("复试方案内容")
    private String FSFA;
    @Alias("审核状态")
    private Integer SHZT;
    @Alias("上传人")
    private String SCR;
    @Alias("上传时间")
    private String SCSJ;
    @Alias("学院审核人")
    private String XYSHR;
    @Alias("学院审核时间")
    private String XYSHSJ;
    @Alias("研院审核人")
    private String YYSHR;
    @Alias("研院审核时间")
    private String YYSHSJ;
    @Alias("更新时间")
    private String GXSJ;
}
