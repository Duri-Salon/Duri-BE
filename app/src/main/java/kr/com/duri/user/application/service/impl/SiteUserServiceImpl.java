package kr.com.duri.user.application.service.impl;

import java.util.Optional;

import kr.com.duri.common.security.jwt.JwtUtil;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.exception.UserNotFoundException;
import kr.com.duri.user.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserServiceImpl implements SiteUserService {

    private final SiteUserRepository siteUserRepository;

    private final JwtUtil jwtUtil;

    @Override
    public Optional<SiteUser> findBySocialId(String socialId) {
        return siteUserRepository.findBySocialId(socialId);
    }

    @Override
    public SiteUser saveNewSiteUser(
            String socialId,
            String email,
            String name,
            String phone,
            String gender,
            String birth,
            String birthYear) {
        SiteUser newSiteUser =
                SiteUser.createNewSiteUser(socialId, email, name, phone, gender, birth, birthYear);
        return siteUserRepository.save(newSiteUser);
    }

    @Override
    public String createNewUserJwt(SiteUser siteUser) {
        return jwtUtil.createJwt(siteUser.getId(), siteUser.getSocialId(), 60 * 60 * 60 * 60L);
    }

    @Override
    public Long getUserIdByToken(String token) {
        token = jwtUtil.removeBearer(token);
        return jwtUtil.getId(token);
    }

    @Override
    public SiteUser getSiteUserById(Long userId) {
        return siteUserRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
