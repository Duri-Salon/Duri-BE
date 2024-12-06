package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.SiteUser;

public interface PetService {

    // 목록 조회
    List<Pet> getPetList(Long userId);

    // 단일 조회
    Pet getPetByUserId(Long userId);

    // petID로 조회
    Pet findById(Long petId);

    Pet createNewPet(SiteUser siteUser, NewPetRequest newPetRequest);

    Pet save(Pet pet);
}
