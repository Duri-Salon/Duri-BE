package kr.com.duri.user.application.facade;

import kr.com.duri.common.security.jwt.JwtUtil;
import kr.com.duri.user.application.dto.response.NewJwtResponse;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final JwtUtil jwtUtil;
    private final SiteUserService siteUserService;

    public NewJwtResponse createNewToken(String providerId) {
        SiteUser siteUser = siteUserService.findBySocialId(providerId).orElseThrow();
        String token = jwtUtil.createJwt(siteUser.getId(), providerId, 60 * 60 * 60 * 60L);
        return NewJwtResponse.builder()
                .client("authorization_user")
                .token(token)
                .newUser(siteUser.getNewUser())
                .build();

    }

}
