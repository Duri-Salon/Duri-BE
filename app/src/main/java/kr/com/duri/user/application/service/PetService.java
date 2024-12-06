package kr.com.duri.user.application.service;

import java.util.Date;
import java.util.List;

import kr.com.duri.user.domain.entity.Pet;

public interface PetService {

    // 목록 조회
    List<Pet> getPetList(Long userId);

    // 단일 조회
    Pet getPetByUserId(Long userId);

    // petID로 조회
    Pet findById(Long petId);

    // 마지막 미용 일자 수정
    void updateLastGromming(Long petId, Date lastDate);
}
