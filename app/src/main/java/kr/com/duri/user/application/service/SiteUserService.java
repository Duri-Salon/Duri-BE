package kr.com.duri.user.application.service;

import java.util.Optional;

import kr.com.duri.user.domain.entity.SiteUser;

public interface SiteUserService {
    Optional<SiteUser> findBySocialId(String socialId);

    SiteUser findById(Long userId);

    SiteUser saveNewSiteUser(
            String socialId,
            String email,
            String name,
            String phone,
            String gender,
            String birth,
            String birthYear);

    String createNewUserJwt(SiteUser siteUser);
}
