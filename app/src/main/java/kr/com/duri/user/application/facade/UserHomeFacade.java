package kr.com.duri.user.application.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.application.mapper.UserHomeMapper;
import kr.com.duri.user.application.service.PetService;
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

    private final UserHomeMapper userHomeMapper;
    private final QuotationService quotationService;
    private final PetService petService;
    private final SiteUserService siteUserService;

    // 사용자 조회
    private SiteUser getUser(Long userId) {
        return siteUserService.findById(userId);
    }

    // TODO : 매장 이미지 조회 서비스 연결
    // private final ShopImageService shopImageService;

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
    public RecentProcedureResponse getRecentProcedure(Long userId) {
        getUser(userId);
        // 1. 반려견, 마지막 미용일로부터 지난일
        Pet pet = petService.findById(userId);
        Integer lastSinceDay =
                (pet.getLastGrooming() != null)
                        ? calculateDateDiff(pet.getLastGrooming(), false)
                        : -1;
        // 2. 견적서, 예약일 디데이
        Quotation quotation = quotationService.getClosetQuoationByUser(userId);
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
}
