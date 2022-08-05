package com.mingmen.yanpro.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RzDao {
    @JsonProperty(value = "ID")
    private Integer ID;
    @JsonProperty(value = "CZRYXM")
    private String CZRYXM;
    @JsonProperty(value = "CZLX")
    private String CZLX;
}
