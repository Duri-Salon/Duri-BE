package kr.com.duri.user.application.facade;

import kr.com.duri.user.application.dto.response.NewUserJwtResponse;
import kr.com.duri.user.application.mapper.UserJwtMapper;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthFacade {

    private final SiteUserService siteUserService;

    private final UserJwtMapper UserJwtMapper;

    public NewUserJwtResponse createNewUserJwt(String providerId) {
        SiteUser siteUser =
                siteUserService
                        .findBySocialId(providerId)
                        .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        String token = siteUserService.createNewUserJwt(siteUser);

        return UserJwtMapper.toNewUserJwtResponse(siteUser, token);
    }
}
