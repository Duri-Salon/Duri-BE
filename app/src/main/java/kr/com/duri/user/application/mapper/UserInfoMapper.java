package kr.com.duri.user.application.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.dto.response.HistoryResponse;
import kr.com.duri.user.application.dto.response.MonthlyHistoryResponse;
import kr.com.duri.user.application.dto.response.SiteUserProfileResponse;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.SiteUser;

import org.springframework.stereotype.Component;

@Component
public class UserInfoMapper {

    // Entity to HistoryResponse DTO
    public HistoryResponse toHistoryResponse(
            Quotation quotation, Groomer groomer, Shop shop, Pet pet, String day) {
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return HistoryResponse.builder()
                .quotationId(quotation.getId())
                .complete(quotation.getComplete())
                .groomerImageURL(groomer.getImage() != null ? groomer.getImage() : "")
                .groomerName(groomer.getName() != null ? groomer.getName() : "")
                .shopId(shop.getId())
                .shopName(shop.getName())
                .petName(pet.getName())
                .day(day != null ? day : "")
                .startDate(
                        quotation.getStartDateTime() != null
                                ? quotation.getStartDateTime().format(dateTimeformatter)
                                : "")
                .build();
    }

    // Entity to MonthlyHistoryResponse DTO
    public MonthlyHistoryResponse toMonthlyHistoryResponse(
            String month, List<HistoryResponse> historyResponseList) {
        return MonthlyHistoryResponse.builder()
                .month(month)
                .historyList(historyResponseList)
                .build();
    }

    public SiteUserProfileResponse toSiteUserProfileResponse(
            SiteUser siteUser, Integer reservationCount, Integer noShowCount) {
        return SiteUserProfileResponse.builder()
                .name(siteUser.getName())
                .email(siteUser.getEmail())
                .phone(siteUser.getPhone())
                .profileImg(siteUser.getImage())
                .reservationCount(reservationCount)
                .noShowCount(noShowCount)
                .stamp(siteUser.getStamp())
                .build();
    }
}
