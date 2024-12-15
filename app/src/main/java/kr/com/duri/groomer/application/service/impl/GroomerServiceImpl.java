package kr.com.duri.groomer.application.service.impl;

import java.util.List;
import java.util.Optional;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.common.s3.S3Util;
import kr.com.duri.groomer.application.dto.request.GroomerDetailRequest;
import kr.com.duri.groomer.application.dto.request.GroomerOnboardingInfo;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.exception.GroomerNotFoundException;
import kr.com.duri.groomer.repository.GroomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GroomerServiceImpl implements GroomerService {

    private final GroomerRepository groomerRepository;

    private final CommonMapper commonMapper;

    private final S3Util s3Util;

    @Override
    public Groomer getGroomerByShopId(Long shopId) {
        return groomerRepository.findByShopId(shopId).stream()
                .findFirst()
                .orElseThrow(() -> new GroomerNotFoundException("해당 매장의 미용사를 찾을 수 없습니다."));
    }

    @Override
    public Groomer createNewGroomer(
            Shop shop, GroomerOnboardingInfo groomerOnboardingInfo, String groomerImageUrl) {
        return groomerRepository.save(
                Groomer.createNewGroomerWithOnboarding(
                        shop,
                        groomerOnboardingInfo.getName(),
                        groomerOnboardingInfo.getAge(),
                        groomerOnboardingInfo.getGender(),
                        groomerOnboardingInfo.getHistory(),
                        groomerImageUrl,
                        commonMapper.toStringJson(groomerOnboardingInfo.getLicense())));
    }

    @Override
    public Groomer createNewGroomer(
            Shop shop, GroomerDetailRequest groomerDetailRequest, String groomerImageUrl) {
        return groomerRepository.save(
                Groomer.createNewGroomer(
                        shop,
                        groomerDetailRequest.getName(),
                        groomerDetailRequest.getAge(),
                        groomerDetailRequest.getGender(),
                        groomerDetailRequest.getEmail(),
                        groomerDetailRequest.getPhone(),
                        groomerDetailRequest.getHistory(),
                        groomerImageUrl,
                        groomerDetailRequest.getInfo(),
                        commonMapper.toStringJson(groomerDetailRequest.getLicense())));
    }

    @Override
    public Groomer findById(Long groomerId) {
        return groomerRepository
                .findById(groomerId)
                .orElseThrow(() -> new GroomerNotFoundException("해당 미용사를 찾을 수 없습니다."));
    }

    @Override
    public Groomer updateGroomer(
            Groomer groomer, GroomerDetailRequest groomerDetailRequest, String groomerImageUrl) {
        String imageUrl =
                groomerImageUrl == null || groomerImageUrl.isEmpty()
                        ? groomer.getImage()
                        : groomerImageUrl;
        return groomerRepository.save(
                groomer.updateGroomerProfile(
                        groomerDetailRequest.getName(),
                        groomerDetailRequest.getAge(),
                        groomerDetailRequest.getGender(),
                        groomerDetailRequest.getEmail(),
                        groomerDetailRequest.getPhone(),
                        groomerDetailRequest.getHistory(),
                        imageUrl,
                        groomerDetailRequest.getInfo(),
                        commonMapper.toStringJson(groomerDetailRequest.getLicense())));
    }

    @Override
    public void deleteGroomer(Groomer groomer) {
        groomerRepository.delete(groomer);
    }

    @Override
    public List<Groomer> findGroomersByShop(Long shopId) {
        return groomerRepository.findByShopId(shopId);
    }

    @Override
    public Optional<Groomer> findGroomerByShopId(Long shopId) {
        return groomerRepository.findByShopId(shopId).stream().findFirst();
    }

    @Override
    public String uploadGroomerImage(MultipartFile img) {
        if (img == null || img.isEmpty()) {
            return null;
        }
        return s3Util.uploadToS3(img, "info");
    }
}
