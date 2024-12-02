package kr.com.duri.user.application.service;

import java.util.Optional;

import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserServiceImpl implements SiteUserService {

    private final SiteUserRepository siteUserRepository;

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
}
