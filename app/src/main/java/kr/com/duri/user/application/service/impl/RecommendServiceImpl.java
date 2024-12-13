package kr.com.duri.user.application.service.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.mapper.PetMapper;
import kr.com.duri.user.application.service.RecommendService;
import kr.com.duri.user.domain.entity.Pet;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final ShopService shopService;
    private final PetMapper petMapper;
    private static final Double RADIUS_20KM = 20000d;
    private static final Double RADIUS_40KM = 40000d;
    private static final Double RADIUS_60KM = 60000d;

    // 성격 Map
    private static final Map<String, String> CHARACTER_MAP =
            Map.of(
                    "character1", "예민해요",
                    "character2", "낯가려요",
                    "character3", "입질이 있어요",
                    "character4", "사람을 좋아해요",
                    "character5", "얌전해요",
                    "character6", "낯선 손길은 무서워요");

    // 질환 Map
    private static final Map<String, String> DISEASE_MAP =
            Map.of(
                    "disease1", "피부 질환",
                    "disease2", "귀 염증",
                    "disease3", "관절 질환",
                    "disease4", "기저 질환",
                    "disease5", "딱히 없어요");

    // 주변 매장 리스트 조회
    public List<Shop> getNearbyShops(Double lat, Double lon) {
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

    // 반려견 - 매장 매칭 점수 계산
    public AbstractMap.SimpleEntry<Integer, String> calculateMatchingScore(
            Pet pet, List<String> shopTags) {
        int score = 0;
        String finalFeature = "";
        List<String> feature = new ArrayList<>();
        // 1. 성격 매칭
        for (String character : petMapper.parseJsonArray(pet.getCharacter())) {
            String mappedCharacter = CHARACTER_MAP.get(character);
            if (mappedCharacter == null) {
                continue; // 매핑되지 않은 경우 스킵
            }
            switch (mappedCharacter) {
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
            String mappedDisease = DISEASE_MAP.get(disease);
            if (mappedDisease == null) {
                continue; // 매핑되지 않은 경우 스킵
            }
            switch (mappedDisease) {
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
    public int getTagMatchScore(List<String> shopTags, String petTag, int matchScore) {
        // 태그 매칭 점수를 계산하는 메소드
        return shopTags.stream().anyMatch(shopTag -> shopTag.contains(petTag)) ? matchScore : 0;
    }

    // 매장 평점 보정
    public Float adjustScoreWithRating(Integer matchingScore, Float shopRating) {
        return matchingScore + (shopRating * 2);
    }
}
