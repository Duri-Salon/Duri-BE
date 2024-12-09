package kr.com.duri.user.application.mapper;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.dto.response.HomePetInfoResponse;
import kr.com.duri.user.application.dto.response.HomeShopResponse;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.application.dto.response.RecommendShopResponse;
import kr.com.duri.user.application.dto.response.RegularShopResponse;
import kr.com.duri.user.domain.Enum.Gender;
import kr.com.duri.user.domain.entity.Pet;

import org.springframework.stereotype.Component;

@Component
public class UserHomeMapper {

    // create Empty RecentProcedureResponse DTO
    public RecentProcedureResponse createEmpty(Long petId, Integer lastSinceDay) {
        return RecentProcedureResponse.builder().petId(petId).lastSinceDay(lastSinceDay).build();
    }

    // Quotation, Pet, Shop, ShopImage Entity to RecentProcedureResponse DTO
    public RecentProcedureResponse toRecentProcedureResponse(
            Quotation quotation,
            Pet pet,
            Shop shop,
            ShopImage shopImage,
            Integer lastSinceDay,
            Integer reserveDday) {
        DateTimeFormatter DayTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh");
        return RecentProcedureResponse.builder()
                .petId(pet.getId())
                .lastSinceDay(lastSinceDay)
                .shopId(shop.getId())
                .imageURL(shopImage.getShopImageUrl() != null ? shopImage.getShopImageUrl() : "")
                .name(shop.getName())
                .address(shop.getAddress())
                .phone(shop.getPhone())
                .kakaoURL(shop.getKakaoTalk() != null ? shop.getKakaoTalk() : "")
                .quotationId(quotation.getId())
                .reserveDday(reserveDday)
                .reservationDate(
                        quotation.getStartDateTime() != null
                                ? quotation.getStartDateTime().format(DayTimeFormatter)
                                : "")
                .price(quotation.getPrice())
                .build();
    }

    // Shop, shopImage Entity to HomeShopResponse DTO
    public HomeShopResponse toHomeShopResponse(
            Shop shop, ShopImage shopImage, Integer reviewCnt, Integer visitCnt) {
        return HomeShopResponse.builder()
                .shopId(shop.getId())
                .imageURL(shopImage.getShopImageUrl() != null ? shopImage.getShopImageUrl() : "")
                .shopName(shop.getName())
                .rating(shop.getRating())
                .reviewCnt(reviewCnt)
                .visitCnt(visitCnt)
                .build();
    }

    // Pet Entity, HomeShopResponse List<DTO> to RegularShopResponse DTO
    public RegularShopResponse toRegularShopResponse(Pet pet, List<HomeShopResponse> homeShopList) {
        return RegularShopResponse.builder()
                .petId(pet.getId())
                .petName(pet.getName())
                .homeShopList(homeShopList)
                .build();
    }

    // Pet Entity to HomePetInfoResponse DTO
    public HomePetInfoResponse toHomePetInfoResponse(Pet pet, String gender) {
        SimpleDateFormat dayTimeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return HomePetInfoResponse.builder()
                .petId(pet.getId())
                .imageURL(pet.getImage() != null ? pet.getImage() : "")
                .name(pet.getName())
                .breed(pet.getBreed() != null ? pet.getBreed() : "")
                .gender(gender != null ? gender : "")
                .age(pet.getAge())
                .weight(pet.getWeight() != null ? pet.getWeight() : 0F)
                .lastGrooming(
                        pet.getLastGrooming() != null
                                ? dayTimeFormatter.format(pet.getLastGrooming())
                                : "")
                .build();
    }

    // Pet, Shop Entity to RecommendShopResponse DTO
    public RecommendShopResponse toRecommendShopResponse(
            Pet pet,
            String feature,
            Shop shop,
            ShopImage shopImage,
            List<String> shopTagsStr,
            Float score) {
        return RecommendShopResponse.builder()
                .petId(pet.getId())
                .recommendFeature(feature)
                .shopId(shop.getId())
                .imageURL(shopImage.getShopImageUrl() != null ? shopImage.getShopImageUrl() : "")
                .shopName(shop.getName())
                .address(shop.getAddress())
                .phone(shop.getPhone() != null ? shop.getPhone() : "")
                .shopTag1(shopTagsStr.size() > 0 ? shopTagsStr.get(0) : "")
                .shopTag2(shopTagsStr.size() > 1 ? shopTagsStr.get(1) : "")
                .score(score != null ? score : 0F)
                .build();
    }

    // 성별 한글로 변환
    public String translateGender(Gender gender) {
        switch (gender) {
            case F:
                return "여아";
            case M:
                return "남아";
            default:
                throw new IllegalArgumentException("잘못된 성별입니다.");
        }
    }
}
