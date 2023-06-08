 package org.smms.authorization.config;

 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;

 import feign.RequestInterceptor;

 @Configuration
 public class ProfileClientConfig {
    
     @Bean
     public RequestInterceptor requestInterceptor() {
         return requestTemplate -> {
             requestTemplate.header("X-Feign-Client", "true");
         };
     }
 }
