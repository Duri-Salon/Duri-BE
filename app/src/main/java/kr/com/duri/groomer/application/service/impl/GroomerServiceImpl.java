package kr.com.duri.groomer.application.service.impl;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.application.dto.request.GroomerOnboardingInfo;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.exception.GroomerNotFoundException;
import kr.com.duri.groomer.repository.GroomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroomerServiceImpl implements GroomerService {

    private final GroomerRepository groomerRepository;

    private final CommonMapper commonMapper;

    @Override
    public Groomer getGroomerByShopId(Long shopId) {
        return groomerRepository.findByShopId(shopId).stream()
                .findFirst()
                .orElseThrow(() -> new GroomerNotFoundException("해당 매장의 미용사를 찾을 수 없습니다."));
    }

    @Override
    public Groomer createNewGroomer(Shop shop, GroomerOnboardingInfo groomerOnboardingInfo) {
        String licenseStringJson = commonMapper.toStringJson(groomerOnboardingInfo.getLicense());
        return groomerRepository.save(
                Groomer.createNewGroomerWithOnboarding(
                        shop,
                        groomerOnboardingInfo.getName(),
                        groomerOnboardingInfo.getAge(),
                        groomerOnboardingInfo.getGender(),
                        groomerOnboardingInfo.getHistory(),
                        groomerOnboardingInfo.getProfileImage(),
                        licenseStringJson));
    }
}
