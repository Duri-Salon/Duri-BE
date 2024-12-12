package kr.com.duri.user.application.service;

import java.util.AbstractMap;
import java.util.List;

import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.domain.entity.Pet;

public interface RecommendService {
    List<Shop> getNearbyShops(Double lat, Double lon);

    AbstractMap.SimpleEntry<Integer, String> calculateMatchingScore(Pet pet, List<String> shopTags);

    int getTagMatchScore(List<String> shopTags, String petTag, int matchScore);

    Float adjustScoreWithRating(Integer matchingScore, Float shopRating);
}
