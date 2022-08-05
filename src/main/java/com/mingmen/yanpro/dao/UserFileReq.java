package com.mingmen.yanpro.dao;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserFileReq {
    private String yxsdm;
    private String xm;
    private String nf;
    private MultipartFile file;
}
