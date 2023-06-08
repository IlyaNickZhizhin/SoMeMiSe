 package org.smms.authorization.feign;

 import org.smms.authorization.config.ProfileClientConfig;
 import org.smms.authorization.dto.ProfileDto;
 import org.springframework.cloud.openfeign.FeignClient;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;

 @FeignClient(name = "profile-client", url = "localhost:8089/api/profile", configuration = ProfileClientConfig.class)
 public interface ProfileClient {
    
     /**
      * @param id {@link org.smms.authorization.entity.UserEntity}
      * @return profile {@link ProfileDto}
      */
     @GetMapping("/read/{id}")
     ProfileDto getProfileById(@PathVariable Long id);

 }
