package kr.com.duri.user.application.service.impl;

import java.util.Date;
import java.util.List;

import kr.com.duri.common.s3.S3Util;
import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.application.mapper.PetMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.exception.PetNotFoundException;
import kr.com.duri.user.repository.PetRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    private final PetMapper petMapper;

    private final S3Util s3Util;

    // [1] 목록 조회
    @Override
    public List<Pet> getPetList(Long userId) {
        return petRepository.findByUserId(userId);
    }

    // [1-1] 단일 조회
    @Override
    public Pet getPetByUserId(Long userId) {
        return getPetList(userId).stream()
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException("해당 고객의 반려견을 찾을 수 없습니다."));
    }

    // petID로 조회
    @Override
    public Pet findById(Long petId) {
        return petRepository
                .findById(petId)
                .orElseThrow(() -> new PetNotFoundException("애완견 ID를 찾을 수 없습니다."));
    }

    @Override
    public Pet createNewPet(SiteUser siteUser, NewPetRequest newPetRequest) {
        String characterStringJson = petMapper.toStringJson(newPetRequest.getCharacter());
        String diseasesStringJson = petMapper.toStringJson(newPetRequest.getDiseases());
        return Pet.createNewPet(
                siteUser,
                newPetRequest.getName(),
                newPetRequest.getBreed(),
                newPetRequest.getAge(),
                newPetRequest.getWeight(),
                newPetRequest.getGender(),
                newPetRequest.getNeutering(),
                characterStringJson,
                diseasesStringJson);
    }

    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    // 마지막 미용 일자 수정
    @Override
    public void updateLastGromming(Long petId, Date lastDate) {
        Pet pet = findById(petId);
        pet.updateLastGromming(lastDate);
        petRepository.save(pet);
    }

    @Override
    public Pet updatePet(Pet pet, NewPetRequest newPetRequest, String imageUrl) {
        return pet.updatePet(newPetRequest.getName(), newPetRequest.getBreed(), newPetRequest.getAge(),
                newPetRequest.getWeight(), newPetRequest.getGender(), newPetRequest.getNeutering(),
                petMapper.toStringJson(newPetRequest.getCharacter()),
                petMapper.toStringJson(newPetRequest.getDiseases()), imageUrl);
    }

    @Override
    public String uploadToS3(MultipartFile img) {
        if (img == null || img.isEmpty()) {
            return null;
        }
        return s3Util.uploadToS3(img);
    }
}
