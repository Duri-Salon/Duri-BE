package kr.com.duri.user.application.mapper;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.dto.response.HistoryResponse;
import kr.com.duri.user.application.dto.response.MonthlyHistoryResponse;
import kr.com.duri.user.application.dto.response.SiteUserProfileResponse;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.domain.entity.SiteUser;

import org.springframework.stereotype.Component;

@Component
public class UserInfoMapper {
    private String safeGet(String value) {
        return value == null ? "" : value;
    }

    // Entity to HistoryResponse DTO
    public HistoryResponse toHistoryResponse(
            Request request, Quotation quotation, Groomer groomer, Shop shop, Pet pet, String day) {
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return HistoryResponse.builder()
                .requestId(request.getId())
                .quotationId(quotation.getId())
                .complete(quotation.getComplete() != null ? quotation.getComplete() : false)
                .groomerImageURL(safeGet(groomer.getImage()))
                .groomerName(groomer.getName())
                .shopId(shop.getId())
                .shopName(shop.getName())
                .petName(pet.getName())
                .day(safeGet(day))
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

    // 날짜 변환
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
}
