package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.request.GroomerOnboardingInfo;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;

public interface GroomerService {
    Groomer getGroomerByShopId(Long shopId);

    Groomer createNewGroomer(Shop shop, GroomerOnboardingInfo groomerOnboardingInfo);
}
