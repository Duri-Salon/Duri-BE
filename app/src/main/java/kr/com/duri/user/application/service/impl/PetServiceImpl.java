package kr.com.duri.user.application.service.impl;

import java.util.Date;
import java.util.List;

import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.exception.PetNotFoundException;
import kr.com.duri.user.repository.PetRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

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

    // 마지막 미용 일자 수정
    @Override
    public void updateLastGromming(Long petId, Date lastDate) {
        Pet pet = findById(petId);
        pet.updateLastGromming(lastDate);
        petRepository.save(pet);
    }
}
