package kr.com.duri.groomer.application.service;

import java.util.Optional;

import kr.com.duri.groomer.application.dto.request.GroomerDetailRequest;
import kr.com.duri.groomer.application.dto.request.GroomerOnboardingInfo;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;

public interface GroomerService {
    Groomer getGroomerByShopId(Long shopId);

    Groomer createNewGroomer(Shop shop, GroomerOnboardingInfo groomerOnboardingInfo);

    Groomer createNewGroomer(Shop shop, GroomerDetailRequest groomerDetailRequest);

    Groomer findById(Long groomerId);

    Groomer updateGroomer(Groomer groomer, GroomerDetailRequest groomerDetailRequest);

    void deleteGroomer(Groomer groomer);

    Optional<Groomer> findGroomerByShopId(Long shopId);
}
