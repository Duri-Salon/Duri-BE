package kr.com.duri.user.application.facade;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.application.mapper.PetMapper;
import kr.com.duri.user.application.mapper.UserInfoMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.Enum.Day;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.SiteUser;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UserInfoFacade {

    private final SiteUserService siteUserService;
    private final PetService petService;
    private final GroomerService groomerService;
    private final QuotationService quotationService;
    private final UserInfoMapper userInfoMapper;
    private final PetMapper petMapper;

    // 견적서로 매장 조회
    private Shop getShopByQuotation(Quotation quotation) {
        return Optional.ofNullable(quotation.getRequest().getShop())
                .orElseThrow(() -> new ShopNotFoundException("해당 매장을 찾을 수 없습니다."));
    }

    public PetProfileResponse createNewPet(String token, NewPetRequest newPetRequest) {
        Long userId = siteUserService.getUserIdByToken(token);
        SiteUser siteUser = siteUserService.getSiteUserById(userId);
        Pet pet =
                petService.save(
                        petService.createNewPet(
                                siteUserService.updateNewUser(siteUser, false), newPetRequest));
        return petMapper.toPetProfileResponse(pet);
    }

    // 고객의 이용기록 조회
    public List<HistoryResponse> getHistoryList(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        Pet pet = petService.getPetByUserId(userId);
        // 1) 이용 기록
        List<Quotation> quotationList = quotationService.getHistoryByPetId(pet.getId());
        if (quotationList.isEmpty()) { // 해당 견적서 없음
            return Collections.emptyList();
        }
        return quotationList.stream()
                .map(
                        quotation -> {
                            // 2) 요일
                            DayOfWeek dayWeek = quotation.getStartDateTime().getDayOfWeek();
                            Day dayMap = Day.from(dayWeek);
                            String day = dayMap.getDay();
                            // 3) 미용사
                            Shop shop = getShopByQuotation(quotation);
                            Groomer groomer = groomerService.getGroomerByShopId(shop.getId());
                            // DTO 변환
                            return userInfoMapper.toHistoryResponse(
                                    quotation, groomer, shop, pet, day);
                        })
                .collect(Collectors.toList());
    }

    // 월별 이용기록 도출
    public List<MonthlyHistoryResponse> getMonthlyHistory(
            List<HistoryResponse> historyResponseList) {
        if (historyResponseList.isEmpty()) { // 기록 없음
            return Collections.emptyList();
        }
        Map<String, List<HistoryResponse>> groupByMonth =
                historyResponseList.stream()
                        .collect(
                                Collectors.groupingBy(
                                        history -> history.getStartDate().substring(0, 7)));
        return groupByMonth.entrySet().stream()
                .map(
                        entry ->
                                userInfoMapper.toMonthlyHistoryResponse(
                                        entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(MonthlyHistoryResponse::getMonth).reversed())
                .collect(Collectors.toList());
    }

    public PetProfileListResponse getPetList(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        List<Pet> petList = petService.getPetList(userId);
        return petMapper.toPetProfileListResponse(petList);
    }

    public PetProfileResponse getPetDetail(Long petId) {
        Pet pet = petService.findById(petId);
        return petMapper.toPetProfileResponse(pet);
    }

    public PetProfileResponse updateNewPet(
            Long petId, NewPetRequest newPetRequest, MultipartFile img) {
        Pet pet = petService.findById(petId);
        String imageUrl = petService.uploadToS3(img);
        pet = petService.save(petService.updatePet(pet, newPetRequest, imageUrl));
        return petMapper.toPetProfileResponse(pet);
    }

    public SiteUserProfileResponse getUserProfile(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        SiteUser siteUser = siteUserService.getSiteUserById(userId);
        Pet pet = petService.getPetByUserId(userId);
        Integer reservationCount = quotationService.getApprovedHistoryByPetId(pet.getId()).size();
        Integer noShowCount = quotationService.getNoShowHistoryByPetId(pet.getId()).size();
        return userInfoMapper.toSiteUserProfileResponse(siteUser, reservationCount, noShowCount);
    }

    public void updateUserProfile(String token, MultipartFile img) {
        Long userId = siteUserService.getUserIdByToken(token);
        SiteUser siteUser = siteUserService.getSiteUserById(userId);
        String imageUrl = siteUserService.uploadToS3(img);
        siteUserService.save(siteUserService.updateProfile(siteUser, imageUrl));
    }
}
