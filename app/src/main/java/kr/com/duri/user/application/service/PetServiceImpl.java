package kr.com.duri.user.application.service;

import java.util.List;

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
}
