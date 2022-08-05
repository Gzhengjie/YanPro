package com.mingmen.yanpro.common;

public enum ValidationEnum {
    LOGIN(1),FORGET_PASS(2);

    public Integer getCode() {
        return code;
    }

    private final Integer code;

    ValidationEnum(Integer code) {
        this.code = code;
    }
}
