package com.mingmen.yanpro.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 院系所表
 * </p>
 *
 * @author gzj
 * @since 2022-07-07
 */
@Getter
@Setter
@TableName("base_college")
@ApiModel(value = "College对象", description = "院系所表")
public class College implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("院系所代码")
    private String yxsdm;

    @ApiModelProperty("院系所名称")
    private String yxsmc;

    @ApiModelProperty("备注")
    private String bz;

}
