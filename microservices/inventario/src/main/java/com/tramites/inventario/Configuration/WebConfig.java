package com.tramites.inventario.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@Configuration
public class WebConfig implements WebMvcConfigurer {
    private LoggingInterceptor loggingInterceptor;
    public WebConfig(LoggingInterceptor loggingInterceptor){
        this.loggingInterceptor=loggingInterceptor;
    }
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
    }
}
