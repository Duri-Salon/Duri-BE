package kr.com.duri.user.application.service;

import java.util.Optional;

import kr.com.duri.user.domain.entity.SiteUser;
import org.springframework.web.multipart.MultipartFile;

public interface SiteUserService {
    Optional<SiteUser> findBySocialId(String socialId);

    SiteUser saveNewSiteUser(
            String socialId,
            String email,
            String name,
            String phone,
            String gender,
            String birth,
            String birthYear);

    String createNewUserJwt(SiteUser siteUser);

    Long getUserIdByToken(String token);

    SiteUser getSiteUserById(Long userId);

    SiteUser updateNewUser(SiteUser siteUser, Boolean newUser);

    String uploadToS3(MultipartFile img);

    SiteUser updateProfile(SiteUser siteUser, String imageUrl);

    SiteUser save(SiteUser siteUser);
}
