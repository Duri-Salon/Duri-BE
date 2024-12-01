package kr.com.duri.user.repository;

import java.util.List;

import kr.com.duri.user.domain.entity.Pet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    // 고객 ID 기준 반려견 찾기
    List<Pet> findByUserId(Long userId);
}
