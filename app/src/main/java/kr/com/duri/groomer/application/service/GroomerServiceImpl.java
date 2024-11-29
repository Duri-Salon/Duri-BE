package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.exception.GroomerNotFoundException;
import kr.com.duri.groomer.repository.GroomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroomerServiceImpl implements GroomerService {

    private final GroomerRepository groomerRepository;

    @Override
    public Groomer getGroomerByShopId(Long shopId) {
        return groomerRepository.findByShopId(shopId).stream()
                .findFirst()
                .orElseThrow(() -> new GroomerNotFoundException("해당 매장의 미용사를 찾을 수 없습니다."));
    }

    @Override
    public boolean existsByShopId(Long shopId) {
        return groomerRepository.existsByShopId(shopId);
    }
}
