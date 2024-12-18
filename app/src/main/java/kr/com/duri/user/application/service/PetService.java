package kr.com.duri.user.application.service;

import java.util.Date;
import java.util.List;

import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.SiteUser;

import org.springframework.web.multipart.MultipartFile;

public interface PetService {

    // 목록 조회
    List<Pet> getPetList(Long userId);

    // 단일 조회
    Pet getPetByUserId(Long userId);

    // petID로 조회
    Pet findById(Long petId);

    Pet createNewPet(SiteUser siteUser, NewPetRequest newPetRequest);

    Pet save(Pet pet);

    // 마지막 미용 일자 수정
    void updateLastGromming(Long petId, Date lastDate);

    String uploadToS3(MultipartFile img);

    Pet updatePet(Pet pet, NewPetRequest newPetRequest, String imageUrl);

    Pet getPetByFeedbackId(Long feedbackId);

    void deletePet(Pet pet);
}
