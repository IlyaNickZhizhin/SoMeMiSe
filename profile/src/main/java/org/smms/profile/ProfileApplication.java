package org.smms.profile;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {"org.smms.profile", "org.smms.common"})
public class ProfileApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProfileApplication.class).web(WebApplicationType.SERVLET)
                .run(args);
    }

}
