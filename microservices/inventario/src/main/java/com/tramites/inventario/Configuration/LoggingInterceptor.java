package com.tramites.inventario.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;



//@Component
public class LoggingInterceptor implements HandlerInterceptor {
//private Map<String,String> map = Collections.EMPTY_MAP;
 @Override
 public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                         Object handler) {
     System.out.println(" link: "+request.getRequestURI());
     System.out.println(" method: "+request.getMethod());
     try {
         UrlsCase(request.getRequestURI(),request);
     }
     catch(Exception e){
         System.out.println(e.getMessage());
     }
 return true;
}

public void afterCompletion(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler,
                            Exception ex){

 System.out.println("Request Finalizada");
}
private boolean UrlsCase(String Url,HttpServletRequest request) throws IOException{
     var flag=false;
     switch (Url) {
         case "/App/data_ent" -> flag=validate(request);
     }
     return true;
}
private boolean validate(HttpServletRequest request) throws IOException {
     byte[] body=request.getInputStream().readAllBytes();
     String payload=new String(body, StandardCharsets.UTF_8);
     System.out.println("payload desde el validaror: "+payload);
     return true;
}
}
