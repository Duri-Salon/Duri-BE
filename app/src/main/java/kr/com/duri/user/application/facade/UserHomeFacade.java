package kr.com.duri.user.application.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.application.service.ShopTagService;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.response.HomePetInfoResponse;
import kr.com.duri.user.application.dto.response.HomeShopResponse;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.application.dto.response.RecommendShopResponse;
import kr.com.duri.user.application.dto.response.RegularShopResponse;
import kr.com.duri.user.application.mapper.PetMapper;
import kr.com.duri.user.application.mapper.UserHomeMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.ReviewService;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.exception.RequestNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHomeFacade {

    private static final Double RADIUS_20KM = 20000d;
    private static final Double RADIUS_40KM = 40000d;
    private static final Double RADIUS_60KM = 60000d;
    private static final int MAX_RECOMMEND = 4;

    private final UserHomeMapper userHomeMapper;
    private final PetMapper petMapper;
    private final QuotationService quotationService;
    private final PetService petService;
    private final SiteUserService siteUserService;
    private final ShopService shopService;
    private final ReviewService reviewService;
    private final ShopTagService shopTagService;

    // TODO : 매장 이미지 조회 서비스 연결
    // private final ShopImageService shopImageService;

    // 사용자 조회
    private SiteUser getUser(Long userId) {
        return siteUserService.getSiteUserById(userId);
    }

    // 견적서로 요청 조회
    private Request getRequestByQuotation(Quotation quotation) {
        return Optional.ofNullable(quotation.getRequest())
                .orElseThrow(() -> new RequestNotFoundException("해당 요청을 찾을 수 없습니다."));
    }

    // 요청으로 매장 조회
    private Shop getShopByRequest(Request request) {
        return Optional.ofNullable(request.getShop())
                .orElseThrow(() -> new ShopNotFoundException("해당 매장을 찾을 수 없습니다."));
    }

    // 오늘로부터 지난일자 계산
    private Integer calculateDateDiff(Object date, boolean isFuture) {
        LocalDate targetDate, today = LocalDate.now();
        // 1. 형식 변환
        if (date instanceof Date) { // Date 형식일 경우
            targetDate = ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (date instanceof LocalDateTime) { // LocalDateTime 형식일 경우
            targetDate = ((LocalDateTime) date).toLocalDate();
        } else {
            throw new IllegalArgumentException("잘못된 형식의 날짜가 입력되었습니다.");
        }
        // 2. 일수 계산
        int dayDifference =
                (int)
                        (isFuture
                                ? targetDate.toEpochDay() - today.toEpochDay()
                                : targetDate.toEpochDay() - today.toEpochDay());
        return dayDifference >= 0 ? dayDifference : -1;
    }

    // 마지막 미용일자 및 최근 예약정보 조회
    public RecentProcedureResponse getRecentProcedure(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        getUser(userId);
        // 1. 반려견, 마지막 미용일로부터 지난일
        Pet pet = petService.findById(userId);
        Integer lastSinceDay =
                (pet.getLastGrooming() != null)
                        ? calculateDateDiff(pet.getLastGrooming(), false)
                        : -1;
        // 2. 견적서, 예약일 디데이
        Quotation quotation = quotationService.getClosetQuoationByUserId(userId);
        if (quotation == null) { // 해당 견적서 없음
            return userHomeMapper.createEmpty(pet.getId(), lastSinceDay);
        }
        Integer reserveDday =
                (quotation.getStartDateTime() != null)
                        ? calculateDateDiff(quotation.getStartDateTime(), true)
                        : -1;
        // 3. 매장
        Request request = getRequestByQuotation(quotation); // 견적서로 요청 조회
        Shop shop = getShopByRequest(request); // 요청으로 매장 조회
        // 4. TODO : 매장 이미지 조회 함수 호출
        ShopImage shopImage = new ShopImage();
        return userHomeMapper.toRecentProcedureResponse(
                quotation, pet, shop, shopImage, lastSinceDay, reserveDday);
    }

    // 단골샵 조회
    public RegularShopResponse getRegularShops(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        getUser(userId);
        // 1. 사용자 아이디로 반려견 조회
        Pet pet = petService.getPetByUserId(userId);
        Long petId = pet.getId();
        // 2. 반려견 아이디로 단골샵 매장 (3번 이상, 가장 많은 방문횟수) 조회
        List<Object[]> regularVisitInfo = quotationService.getRegularInfoByPetId(petId);
        if (regularVisitInfo.isEmpty()) { // 단골샵 없음
            return userHomeMapper.toRegularShopResponse(pet, Collections.emptyList());
        }
        // 3. HomeShopResponse 리스트 생성
        List<HomeShopResponse> homeShopList =
                regularVisitInfo.stream()
                        .map(
                                info -> {
                                    // 매장
                                    Long shopId = (Long) info[0];
                                    Shop shop = shopService.findById(shopId);
                                    // 4. TODO : 매장 이미지
                                    ShopImage shopImage =
                                            new ShopImage(); // shopImageService.getByShopId(shop.getId());
                                    Integer reviewCnt =
                                            reviewService
                                                    .getReviewsByShopId(shop.getId())
                                                    .size(); // 리뷰 개수
                                    Integer visitCnt = ((Number) info[1]).intValue(); // 방문 횟수
                                    return userHomeMapper.toHomeShopResponse(
                                            shop, shopImage, reviewCnt, visitCnt);
                                })
                        .collect(Collectors.toList());

        // 4. RegularShopResponse 변환
        return userHomeMapper.toRegularShopResponse(pet, homeShopList);
    }

    // 매장 추천
    public List<RecommendShopResponse> getRecommendShops(String token, Double lat, Double lon) {
        Long userId = siteUserService.getUserIdByToken(token);
        Pet pet = petService.findById(userId);
        // 1. 주변 매장 계산
        List<Shop> nearbyShops = getNearbyShops(lat, lon);
        if (nearbyShops.isEmpty()) {
            return Collections.emptyList();
        }
        // 2. 반려견-매장 매칭 점수
        List<RecommendShopResponse> recommendations =
                nearbyShops.stream()
                        .map(
                                shop -> {
                                    // 1) 매장 태그 가져오기
                                    List<String> shopTags =
                                            shopTagService.findTagsByShopId(shop.getId());
                                    // 2) 성격, 질환, 나이, 크기 기준 매칭 점수 및 기준 도출
                                    AbstractMap.SimpleEntry<Integer, String> scoreMap =
                                            calculateMatchingScore(pet, shopTags);
                                    Integer score = scoreMap.getKey();
                                    String feature = scoreMap.getValue();
                                    // 3) 평점 보정
                                    Float adjustScore =
                                            adjustScoreWithRating(score, shop.getRating());
                                    // 4) TODO : 매장 이미지 조회 연결
                                    ShopImage shopImage =
                                            new ShopImage(); // shopImageService.getImagesByShopId(shopId);
                                    return userHomeMapper.toRecommendShopResponse(
                                            pet, feature, shop, shopImage, shopTags, adjustScore);
                                })
                        .collect(Collectors.toList());

        // 3. 매칭 점수 기준 정렬해 상위 2개 반환
        return recommendations.stream()
                .sorted(Comparator.comparingDouble(RecommendShopResponse::getScore).reversed())
                .limit(MAX_RECOMMEND)
                .collect(Collectors.toList());
    }

    // 주변 매장 리스트 조회
    private List<Shop> getNearbyShops(Double lat, Double lon) {
        // 1. 지역 기반 필터링 (20km -> 40km -> 60km)
        List<Shop> shops = shopService.findShopsByRadius(lat, lon, RADIUS_20KM);
        if (shops.size() < 2) {
            shops = shopService.findShopsByRadius(lat, lon, RADIUS_40KM);
            if (shops.size() < 2) {
                shops = shopService.findShopsByRadius(lat, lon, RADIUS_60KM);
            }
        }
        return shops;
    }

    // TODO : 매장 태그 키워드 디자인 후 변경 시 수정 필요
    // 반려견 - 매장 매칭 점수 계산
    private AbstractMap.SimpleEntry<Integer, String> calculateMatchingScore(
            Pet pet, List<String> shopTags) {
        int score = 0;
        String finalFeature = "";
        List<String> feature = new ArrayList<>();
        // 1. 성격 매칭
        for (String character : petMapper.parseJsonArray(pet.getCharacter())) {
            switch (character) {
                case "예민해요":
                    score += getTagMatchScore(shopTags, "예민한 반려견", 10);
                    score += getTagMatchScore(shopTags, "스트레스", 9);
                    feature.add("예민한");
                    break;
                case "입질이 있어요":
                    score += getTagMatchScore(shopTags, "예민한 반려견", 8);
                    score += getTagMatchScore(shopTags, "스트레스", 6);
                    feature.add("입질이 있는");
                    break;
                case "낯가려요":
                case "낯선 손길은 무서워요":
                    score += getTagMatchScore(shopTags, "예민한 반려견", 5);
                    score += getTagMatchScore(shopTags, "스트레스", 10);
                    feature.add("낯 가리는");
                    break;
                case "얌전해요":
                    score += getTagMatchScore(shopTags, "예민한 반려견", 1);
                    score += getTagMatchScore(shopTags, "활발", 2);
                    score += getTagMatchScore(shopTags, "스트레스", 1);
                    feature.add("얌전한");
                    break;
                case "사람을 좋아해요":
                    score += getTagMatchScore(shopTags, "활발", 10);
                    feature.add("활발한");
                default:
                    break;
            }
        }
        // 2. 질환 매칭
        for (String disease : petMapper.parseJsonArray(pet.getDiseases())) {
            switch (disease) {
                case "피부 질환":
                case "귀 염증":
                    score += getTagMatchScore(shopTags, "예민한 반려견", 8);
                    score += getTagMatchScore(shopTags, "피부", 2);
                    score += getTagMatchScore(shopTags, "스트레스", 2);
                    feature.add("피부가 예민한");
                    break;
                case "관절 질환":
                case "기저 질환":
                    score += getTagMatchScore(shopTags, "예민", 3);
                    score += getTagMatchScore(shopTags, "스트레스", 3);
                    feature.add("질환이 있는");
                    break;
                default:
                    break;
            }
        }
        // 3. 나이
        int age = pet.getAge();
        if (age >= 8) {
            score += getTagMatchScore(shopTags, "노견", 7);
            feature.add("노견인");
        }
        // 4. 크기
        Float weight = pet.getWeight();
        if (weight <= 9) {
            score += getTagMatchScore(shopTags, "소형", 5);
            feature.add("소형견인");
        } else if (weight <= 24) {
            score += getTagMatchScore(shopTags, "중형", 5);
            feature.add("중형견인");
        } else if (weight <= 44) {
            score += getTagMatchScore(shopTags, "대형", 5);
            feature.add("대형견인");
        } else {
            score += getTagMatchScore(shopTags, "대형", 7);
        }
        // 5. 특성 계산
        if (!feature.isEmpty()) {
            Random random = new Random();
            int randomIdx = random.nextInt(feature.size());
            finalFeature = feature.get(randomIdx);
        }
        return new AbstractMap.SimpleEntry<>(score, finalFeature);
    }

    // 태그 매칭 점수 계산
    private int getTagMatchScore(List<String> shopTags, String petTag, int matchScore) {
        // 태그 매칭 점수를 계산하는 메소드
        return shopTags.stream().anyMatch(shopTag -> shopTag.contains(petTag)) ? matchScore : 0;
    }

    // 매장 평점 보정
    private Float adjustScoreWithRating(Integer matchingScore, Float shopRating) {
        return matchingScore + (shopRating * 2);
    }

    // 펫 간단 정보 조회
    public HomePetInfoResponse getPetInfo(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        getUser(userId);
        Pet pet = petService.getPetByUserId(userId);
        String gender = userHomeMapper.translateGender(pet.getGender());
        return userHomeMapper.toHomePetInfoResponse(pet, gender);
    }
}
