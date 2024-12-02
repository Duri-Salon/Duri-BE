package kr.com.duri.user.repository;

import java.util.Optional;

import kr.com.duri.user.domain.entity.SiteUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findBySocialId(String socialId);
}
