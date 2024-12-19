package kr.com.duri.user.application.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.common.s3.S3Util;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopImageService;
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
import kr.com.duri.user.application.mapper.UserHomeMapper;
import kr.com.duri.user.application.service.AiService;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.RecommendService;
import kr.com.duri.user.application.service.ReviewService;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.exception.RequestNotFoundException;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class UserHomeFacade {

    private static final int MAX_RECOMMEND = 4;
    private final UserHomeMapper userHomeMapper;
    private final QuotationService quotationService;
    private final PetService petService;
    private final SiteUserService siteUserService;
    private final ShopService shopService;
    private final ReviewService reviewService;
    private final ShopTagService shopTagService;
    private final AiService aiService;
    private final RecommendService recommendService;
    private final ShopImageService shopImageService;
    private final S3Util s3Util;

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
        // 1) 형식 변환
        if (date instanceof Date) { // Date 경우
            targetDate = ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (date instanceof LocalDateTime) { // LocalDateTime 경우
            targetDate = ((LocalDateTime) date).toLocalDate();
        } else {
            throw new IllegalArgumentException("잘못된 형식의 날짜가 입력되었습니다.");
        }
        // 2) 일수 계산
        int dayDifference =
                (int)
                        (isFuture
                                ? targetDate.toEpochDay() - today.toEpochDay()
                                : targetDate.toEpochDay() - today.toEpochDay());
        return dayDifference;
    }

    // JSON 내 totalPrice 추출
    private Integer extractTotalPrice(String priceJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(priceJson);
            return jsonNode.get("totalPrice").asInt();
        } catch (Exception e) { // 형식이 잘못되었을 경우
            throw new IllegalArgumentException("잘못된 형식입니다 - " + priceJson, e);
        }
    }

    // 마지막 미용일자 및 최근 예약정보 조회
    public RecentProcedureResponse getRecentProcedure(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        getUser(userId);
        // 1) 반려견, 마지막 미용일로부터 지난일
        Pet pet = petService.getPetByUserId(userId);
        Integer lastSinceDay =
                (pet.getLastGrooming() != null)
                        ? Math.abs(calculateDateDiff(pet.getLastGrooming(), false))
                        : -1;
        // 2) 견적서, 예약일 디데이
        Quotation quotation = quotationService.getClosetQuoationByUserId(userId);
        if (quotation == null) { // 해당 견적서 없음
            return userHomeMapper.createEmpty(pet.getId(), lastSinceDay);
        }
        Integer reserveDday =
                (quotation.getStartDateTime() != null)
                        ? calculateDateDiff(quotation.getStartDateTime(), true)
                        : -1;
        Integer totalPrice = extractTotalPrice(quotation.getPrice());
        // 3) 매장
        Request request = getRequestByQuotation(quotation); // 견적서로 요청 조회
        Shop shop = getShopByRequest(request); // 요청으로 매장 조회
        // 4) 매장 이미지
        ShopImage shopImage = shopImageService.getMainShopImage(shop);
        return userHomeMapper.toRecentProcedureResponse(
                quotation, pet, shop, shopImage, lastSinceDay, reserveDday, totalPrice);
    }

    // 단골샵 조회
    public RegularShopResponse getRegularShops(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        getUser(userId);
        // 1) 반려견 조회
        Pet pet = petService.getPetByUserId(userId);
        Long petId = pet.getId();
        // 2) 반려견 아이디로 단골샵 매장 (3번 이상, 가장 많은 방문횟수) 조회
        List<Object[]> regularVisitInfo = quotationService.getRegularInfoByPetId(petId);
        if (regularVisitInfo.isEmpty()) { // 단골샵 없음
            return userHomeMapper.toRegularShopResponse(pet, Collections.emptyList());
        }
        List<HomeShopResponse> homeShopList =
                regularVisitInfo.stream()
                        .map(
                                info -> {
                                    // 3) 매장
                                    Long shopId = (Long) info[0];
                                    Shop shop = shopService.findById(shopId);
                                    // 4) 매장 이미지
                                    ShopImage shopImage = shopImageService.getMainShopImage(shop);
                                    Integer reviewCnt =
                                            reviewService
                                                    .getReviewsByShopId(shop.getId())
                                                    .size(); // 리뷰 개수
                                    Integer visitCnt = ((Number) info[1]).intValue(); // 방문 횟수
                                    return userHomeMapper.toHomeShopResponse(
                                            shop, shopImage, reviewCnt, visitCnt);
                                })
                        .collect(Collectors.toList());
        return userHomeMapper.toRegularShopResponse(pet, homeShopList);
    }

    // 매장 추천 (로그인 O)
    public List<RecommendShopResponse> getRecommendShops(String token, Double lat, Double lon) {
        Long userId = siteUserService.getUserIdByToken(token);
        Pet pet = petService.getPetByUserId(userId);
        // 1) 주변 매장 계산
        List<Shop> nearbyShops = recommendService.getNearbyShops(lat, lon);
        if (nearbyShops.isEmpty()) {
            return Collections.emptyList();
        }
        // 2) 반려견-매장 매칭 점수
        List<RecommendShopResponse> recommendations =
                nearbyShops.stream()
                        .map(
                                shop -> {
                                    // 3) 매장 태그
                                    List<String> shopTags =
                                            shopTagService.findTagsByShopId(shop.getId());
                                    // 4) 성격, 질환, 나이, 크기 기준 매칭 점수 및 기준 도출
                                    AbstractMap.SimpleEntry<Integer, String> scoreMap =
                                            recommendService.calculateMatchingScore(pet, shopTags);
                                    Integer score = scoreMap.getKey();
                                    String feature = scoreMap.getValue();
                                    // 5) 평점 보정
                                    Float adjustScore =
                                            recommendService.adjustScoreWithRating(
                                                    score, shop.getRating());
                                    // 6) 매장 이미지
                                    ShopImage shopImage = shopImageService.getMainShopImage(shop);
                                    return userHomeMapper.toRecommendShopResponse(
                                            pet, feature, shop, shopImage, shopTags, adjustScore);
                                })
                        .collect(Collectors.toList());
        // 7) 매칭 점수 기준 정렬해 상위 4개 반환
        return recommendations.stream()
                .sorted(Comparator.comparingDouble(RecommendShopResponse::getScore).reversed())
                .limit(MAX_RECOMMEND)
                .collect(Collectors.toList());
    }

    // 매장 추천 (로그인 X)
    public List<RecommendShopResponse> getNearByShopsDistance(Double lat, Double lon) {
        // 1) 매장 반환 현재 위치 거리순 가져오기
        List<Shop> shopResults = shopService.findShopswithSortAsc(lat, lon);
        // 2) DTO 반환
        List<RecommendShopResponse> recommendations =
                shopResults.stream()
                        .map(
                                shop -> {
                                    // 3) 매장 태그
                                    List<String> shopTags =
                                            shopTagService.findTagsByShopId(shop.getId());
                                    // 4) 매장 이미지
                                    ShopImage shopImage = shopImageService.getMainShopImage(shop);
                                    return userHomeMapper.toRecommendShopResponse(
                                        new Pet(), "주변 강아지 친구들", shop, shopImage, shopTags, 0F);
                                })
                        .collect(Collectors.toList());
        // 5) 매칭 점수 기준 정렬해 상위 4개 반환
        return recommendations.stream().limit(MAX_RECOMMEND).collect(Collectors.toList());
    }

    // 펫 간단 정보 조회
    public HomePetInfoResponse getPetInfo(String token) {
        Long userId = siteUserService.getUserIdByToken(token);
        getUser(userId);
        Pet pet = petService.getPetByUserId(userId);
        String gender = userHomeMapper.translateGender(pet.getGender());
        return userHomeMapper.toHomePetInfoResponse(pet, gender);
    }

    // AI 스타일링
    public String getPetAiStyling(String styleText, MultipartFile image) {
        if (image.isEmpty() || image == null) {
            throw new RuntimeException("이미지 입력 누락");
        }
        try {
            // 1) S3 사진 업로드 URL
            String imageURL = s3Util.uploadToS3(image, "ai");
            // 2) 프롬프트, 네거티브, 모델 생성
            String prompt = aiService.generatePrompt(styleText); // 테디베어, 베이비, 라이언
            String negativePrompt = aiService.generateNegativePrompt(styleText);
            String version = aiService.generateModel(styleText);
            // 3) 결과 반환
            String resultImageURL =
                    aiService.callReplicateApi(imageURL, prompt, negativePrompt, version);
            // 4) S3 업로드 삭제
            s3Util.deleteFromS3(imageURL);
            return resultImageURL;
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }
}
