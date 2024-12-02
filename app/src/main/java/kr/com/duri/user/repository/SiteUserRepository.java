package kr.com.duri.user.repository;

import kr.com.duri.user.domain.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findBySocialId(String socialId);
}
