package kr.com.duri.user.application.mapper;

import kr.com.duri.user.application.dto.response.NewUserJwtResponse;

import kr.com.duri.user.domain.entity.SiteUser;
import org.springframework.stereotype.Component;

@Component
public class UserJwtMapper {

    public NewUserJwtResponse toNewUserJwtResponse(SiteUser siteUser, String token) {
        return NewUserJwtResponse.builder()
                .username(siteUser.getName())
                .token(token)
                .client("authorization_user")
                .newUser(siteUser.getNewUser())
                .build();
    }
}
