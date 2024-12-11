package kr.com.duri.user.application.facade;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.application.mapper.PetMapper;
import kr.com.duri.user.application.mapper.UserInfoMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;
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

    // 요일
    public String getDay(DayOfWeek dayWeek) {
        switch (dayWeek) {
            case MONDAY:
                return "월";
            case TUESDAY:
                return "화";
            case WEDNESDAY:
                return "수";
            case THURSDAY:
                return "목";
            case FRIDAY:
                return "금";
            case SATURDAY:
                return "토";
            case SUNDAY:
                return "일";
            default:
                throw new IllegalArgumentException("잘못된 요일입니다.");
        }
    }

    public PetProfileResponse createNewPet(String token, NewPetRequest newPetRequest) {
        Long userId = siteUserService.getUserIdByToken(token);
        SiteUser siteUser = siteUserService.getSiteUserById(userId);
        Pet pet = petService.save(petService.createNewPet(siteUser, newPetRequest));
        return petMapper.toPetProfileResponse(pet);
    }

    // 고객의 이용기록 조회
    public List<HistoryResponse> getHistoryList(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        Pet pet = petService.getPetByUserId(userId);
        // 1. 해당 유저의 이용 기록 가져오기
        List<Quotation> quotationList = quotationService.getHistoryByPetId(pet.getId());
        if (quotationList.isEmpty()) { // 해당 견적서 없음
            return Collections.emptyList();
        }
        return quotationList.stream()
                .map(
                        quotation -> {
                            // 요일
                            DayOfWeek dayWeek = quotation.getStartDateTime().getDayOfWeek();
                            String day = getDay(dayWeek);
                            // 미용사
                            Request request = quotation.getRequest();
                            Shop shop = request.getShop();
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
        // 월별로 그룹화
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
        Integer reservationCount = quotationService.getHistoryByPetId(pet.getId()).size();
        Integer noShowCount = quotationService.getNoShowHistoryByPetId(pet.getId()).size();
        return userInfoMapper.toSiteUserProfileResponse(siteUser, reservationCount, noShowCount);
    }

    public SiteUserProfileResponse updateUserProfile(String token, MultipartFile img) {
        Long userId = siteUserService.getUserIdByToken(token);
        return null;
    }
}
