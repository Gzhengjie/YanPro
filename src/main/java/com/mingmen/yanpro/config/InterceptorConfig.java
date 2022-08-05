package com.mingmen.yanpro.config;

import com.mingmen.yanpro.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //配置类需要加
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  //拦截所有请求，通过判断token是否合法来决定是否需要登录
                .excludePathPatterns("/login", "/register", "/zyzsxx/**", "/ksxxsh/**", "/fsfa/**", "/**/export", "/**/import","/**/outDiaoDangHan"
                ,"/ss_jianzhang_zykskm/ss_jianzhang_pinjie_download","/ss_jianzhang_zykskm/import"
                ,"/ss_jianzhang_zyxwgg/**","/**/loadmb", "/**/pjJZBZ_download","/base_user/**","/tuimian_jsxx/**"
                ,"/tuimian_jszbxx/**","/SsXuanchuanXlyxx/**","/user/**","/**/export1"
                ,"/ss_jianzhang_zszyrs/**")//放行请求
                .excludePathPatterns("/swagger**/**",
                        "/webjars/**",
                        "/v3/**",
                        "/doc.html");
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
