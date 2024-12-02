package kr.com.duri.user.application.service;

import kr.com.duri.user.domain.entity.SiteUser;

import java.util.Optional;

public interface SiteUserService {
    Optional<SiteUser> findBySocialId(String socialId);

    SiteUser saveNewSiteUser(
            String socialId,
            String email,
            String name,
            String phone,
            String gender,
            String birth,
            String birthYear
    );
}