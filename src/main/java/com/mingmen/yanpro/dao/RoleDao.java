package com.mingmen.yanpro.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoleDao {
    @JsonProperty(value = "ID")
    private Integer ID;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "flag")
    private String flag;
}
