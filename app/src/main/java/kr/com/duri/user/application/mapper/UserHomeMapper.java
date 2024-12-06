package kr.com.duri.user.application.mapper;

import java.time.format.DateTimeFormatter;

import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.domain.entity.Pet;

import org.springframework.stereotype.Component;

@Component
public class UserHomeMapper {

    // create Empty RecentProcedureResponse DTO
    public RecentProcedureResponse createEmpty(Long petId, Integer lastSinceDay) {
        return RecentProcedureResponse.builder().petId(petId).lastSinceDay(lastSinceDay).build();
    }

    // Entity to RecentProcedureResponse DTO
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
}
