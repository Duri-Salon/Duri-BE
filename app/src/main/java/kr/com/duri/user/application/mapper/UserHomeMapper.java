package kr.com.duri.user.application.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.dto.response.HomePetInfoResponse;
import kr.com.duri.user.application.dto.response.HomeShopResponse;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.application.dto.response.RegularShopResponse;
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
        return HomePetInfoResponse.builder()
                .petId(pet.getId())
                .imageURL(pet.getImage() != null ? pet.getImage() : "")
                .name(pet.getName())
                .breed(pet.getBreed() != null ? pet.getBreed() : "")
                .gender(gender != null ? gender : "")
                .age(pet.getAge())
                .weight(pet.getWeight() != null ? pet.getWeight() : 0F)
                .build();
    }
}
