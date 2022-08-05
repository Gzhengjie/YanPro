package com.mingmen.yanpro.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SljtgqkDao {
    @JsonProperty(value = "ID")
    private String ID;
    @JsonProperty(value = "TGXMDM")
    private String TGXMDM;
    @JsonProperty(value = "TGXMMC")
    private String TGXMMC;
}
