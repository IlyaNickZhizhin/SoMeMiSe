package org.smms.authorization;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"org.smms.authorization", "org.smms.common"})
@EnableFeignClients(basePackages = {"org.smms.authorization.feign"})
public class AuthorizationApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthorizationApplication.class).web(WebApplicationType.SERVLET)
                .run(args);
    }

}
