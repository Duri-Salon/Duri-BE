package kr.com.duri.groomer.application.facade;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.application.dto.response.RecentProcedureResponse;
import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.TodayScheduleResponse;
import kr.com.duri.groomer.application.mapper.GroomerHomeMapper;
import kr.com.duri.groomer.application.mapper.ShopMapper;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.QuotationReq;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.exception.PetNotFoundException;
import kr.com.duri.user.exception.RequestNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroomerHomeFacade {
    private final ShopService shopService;
    private final QuotationService quotationService;
    private final GroomerService groomerService;
    private final ShopMapper shopMapper;
    private final GroomerHomeMapper groomerHomeMapper;

    // 매장 조회
    private Shop getShop(Long shopId) {
        return shopService.findById(shopId);
    }

    // [1] 매장 정보 조회
    public ShopDetailResponse getShopDetail(Long shopId) {
        Shop shop = getShop(shopId);
        // TODO : 매장 이미지 조회 구현 및 연결
        ShopImage shopImage = new ShopImage(); // shopImageService.?(shopId)
        return shopMapper.toShopDetailResponse(shop, shopImage);
    }

    // [2] 가까운 시술정보 조회
    public RecentProcedureResponse getRecentProcedure(Long shopId) {
        getShop(shopId);
        // 1. 현재 일자로부터 가장 최근의 견적서 조회
        Quotation quotation = quotationService.getClosetQuoation(shopId);
        if (quotation == null) { // 예약 시술 내역 없음
            return RecentProcedureResponse.createEmpty();
        }
        // 2. 견적서의 요청 ID로 요청 객체 불러오기
        Request request =
                Optional.ofNullable(quotation.getRequest())
                        .orElseThrow(() -> new RequestNotFoundException("해당 요청을 찾을 수 없습니다."));
        // 3. 견적 요청서 ID로 반려견 객체 조회
        QuotationReq quotationReq =
                Optional.ofNullable(request.getQuotation())
                        .orElseThrow(() -> new RequestNotFoundException("해당 견적요청서를 찾을 수 없습니다."));
        // 4. 반려견으로 사용자 객체 조회
        Pet pet =
                Optional.ofNullable(quotationReq.getPet())
                        .orElseThrow(() -> new PetNotFoundException("해당 반려견을 찾을 수 없습니다."));
        // 5. 사용자
        SiteUser user = pet.getUser();
        return groomerHomeMapper.toRecentProcedureResponse(pet, user, quotation);
    }

    // [3] 당일 스케줄 조회
    public List<TodayScheduleResponse> getTodaySchedule(Long shopId) {
        getShop(shopId);
        // 1. 매장으로 디자이너 조회
        Groomer groomer = groomerService.getGroomerByShopId(shopId);
        // 2. 오늘 날짜인 해당 매장의 견적서 조회
        List<Quotation> quotationList = quotationService.getTodayQuotations(shopId);
        if (quotationList.isEmpty()) { // 해당 견적서 없음
            return Collections.emptyList();
        }
        return quotationList.stream()
                .map(
                        quotation -> {
                            // 3. 요청으로 반려견 조회
                            Request request = quotation.getRequest();
                            Pet pet = request.getQuotation().getPet();
                            // 4. DTO 변환
                            return groomerHomeMapper.toTodayScheduleResponse(
                                    pet, quotation, groomer);
                        })
                .collect(Collectors.toList());
    }

    // [4] 미용 완료 여부 수정
    public void updateComplete(
            Long quotationId, QuotationUpdateCompleteRequest quotationUpdateCompleteRequest) {
        quotationService.updateComplete(quotationId, quotationUpdateCompleteRequest);
    }
}
