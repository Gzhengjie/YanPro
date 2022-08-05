package com.mingmen.yanpro.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mingmen.yanpro.dao.Validation;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-06-28
 */
public interface IValidationService extends IService<Validation> {

    void saveCode(String email, String code, Integer code1, DateTime offsetMinute);
}
