package com.mingmen.yanpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableCaching
@EnableOpenApi
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
