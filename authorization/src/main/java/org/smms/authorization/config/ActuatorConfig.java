package org.smms.authorization.config;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ActuatorConfig implements InfoContributor {

    Environment environment;
    LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    /**
     * @param builder для создания информации о приложении {@link Info.Builder}
     */
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("version", getProperty("spring.application.version"))
                .withDetail("name", getProperty("spring.application.name"))
                .withDetail("contexPath", getProperty("server.servlet.context-path"))
                .withDetail("startDateTime", startTime);

    }

    private String getProperty(String key) {
        return environment.getProperty(key);
    }
}
