package kr.com.duri.groomer.application.service;

import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.application.dto.request.GroomerDetailRequest;
import kr.com.duri.groomer.application.dto.request.GroomerOnboardingInfo;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;

import org.springframework.web.multipart.MultipartFile;

public interface GroomerService {
    Groomer getGroomerByShopId(Long shopId);

    Groomer createNewGroomer(
            Shop shop, GroomerOnboardingInfo groomerOnboardingInfo, String groomerImageUrl);

    Groomer createNewGroomer(
            Shop shop, GroomerDetailRequest groomerDetailRequest, String groomerImageUrl);

    Groomer findById(Long groomerId);

    Groomer updateGroomer(
            Groomer groomer, GroomerDetailRequest groomerDetailRequest, String groomerImageUrl);

    void deleteGroomer(Groomer groomer);

    List<Groomer> findGroomersByShop(Long shopId);

    Optional<Groomer> findGroomerByShopId(Long shopId);

    String uploadGroomerImage(MultipartFile img);
}
