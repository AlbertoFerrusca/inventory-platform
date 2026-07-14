package com.tramites.inventario.Configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.http.HttpInputMessage;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;

import java.lang.reflect.Type;

//@ControllerAdvice
@Order(1)
@RestControllerAdvice
//@ControllerAdvice

public class LoggingRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @PostConstruct
    public void init() {
        System.out.println("✅ RequestBodyAdvice CARGADO");
    }


    @Override
    public boolean supports(MethodParameter parameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        System.out.println("✅ BODY CAPTURADO: " + body);
        return body; // 🔴 sin esto rompe el controller
    }
}

