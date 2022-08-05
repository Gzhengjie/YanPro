package com.mingmen.yanpro.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//@Configuration // 标明是配置类
//@EnableSwagger2 //开启swagger功能
@Configuration
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)  // DocumentationType.SWAGGER_2 固定的，代表swagger2
//                .groupName("分布式任务系统") // 如果配置多个文档的时候，那么需要配置groupName来分组标识
                .apiInfo(apiInfo()) // 用于生成API信息
                .select() // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
                .apis(RequestHandlerSelectors.basePackage("com.mingmen.yanpro.controller")) // 用于指定扫描哪个包下的接口
                .paths(PathSelectors.any())// 选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .build();
    }

    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("研究生管理系统API") //  可以用来自定义API的主标题
                .description("研究生管理系统项目SwaggerAPI管理") // 可以用来描述整体的API
                .termsOfServiceUrl("") // 用于定义服务的域名
                .version("1.0") // 可以用来定义版本。
                .build(); //
    }
}
///**
// * Swagger配置类
// */
//@Configuration
//@EnableOpenApi
//public class SwaggerConfig {
//    @Bean
//    public Docket docket(){
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .enable(true)
//                .groupName("ZRJ")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.syw.springboot.controller"))
//                .paths(PathSelectors.ant("/controller/**"))
//                .build();
//    }
//
//
//    @SuppressWarnings("all")
//    public ApiInfo apiInfo(){
//        return new ApiInfo(
//                "zrj's api",
//                "redis project",
//                "v1.0",
//                "2261839618@qq.com", //开发者团队的邮箱
//                "ZRJ",
//                "Apache 2.0",  //许可证
//                "http://www.apache.org/licenses/LICENSE-2.0" //许可证链接
//        );
//    }
//}
