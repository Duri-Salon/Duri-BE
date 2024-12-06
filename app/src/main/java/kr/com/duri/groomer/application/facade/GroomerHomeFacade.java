package kr.com.duri.groomer.application.facade;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.application.dto.response.HomeQuotationReqResponse;
import kr.com.duri.groomer.application.dto.response.RecentProcedureResponse;
import kr.com.duri.groomer.application.dto.response.TodayScheduleResponse;
import kr.com.duri.groomer.application.mapper.GroomerHomeMapper;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.service.RequestService;
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
    private final RequestService requestService;
    private final GroomerHomeMapper groomerHomeMapper;

    // 매장 조회
    private Shop getShop(Long shopId) {
        return shopService.findById(shopId);
    }

    // 견적서로 요청 조회
    private Request getRequestByQuotation(Quotation quotation) {
        return Optional.ofNullable(quotation.getRequest())
                .orElseThrow(() -> new RequestNotFoundException("해당 요청을 찾을 수 없습니다."));
    }

    // 요청으로 견적 요청서 조회
    private QuotationReq getQuotationReqByRequest(Request request) {
        return Optional.ofNullable(request.getQuotation())
                .orElseThrow(() -> new RequestNotFoundException("해당 견적요청서를 찾을 수 없습니다."));
    }

    // 견적 요청서로 반려견 조회
    private Pet getPetByQuotationReq(QuotationReq quotationReq) {
        return Optional.ofNullable(quotationReq.getPet())
                .orElseThrow(() -> new PetNotFoundException("해당 반려견을 찾을 수 없습니다."));
    }

    // 가까운 시술정보 조회
    public RecentProcedureResponse getRecentProcedure(Long shopId) {
        getShop(shopId);
        // 1. 현재 일자로부터 가장 최근의 견적서 조회
        Quotation quotation = quotationService.getClosetQuoationByShop(shopId);
        if (quotation == null) { // 예약 시술 내역 없음
            return RecentProcedureResponse.createEmpty();
        }
        // 2. 견적서의 요청 ID로 요청 객체 불러오기
        Request request = getRequestByQuotation(quotation);
        // 3. 요청으로 견적요청서 조회
        QuotationReq quotationReq = getQuotationReqByRequest(request);
        // 4. 견적요청서로 반려견 조회
        Pet pet = getPetByQuotationReq(quotationReq);
        // 5. 반려견으로 사용자 조회
        SiteUser user = pet.getUser();
        return groomerHomeMapper.toRecentProcedureResponse(pet, user, quotation);
    }

    // 당일 스케줄 조회
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
                            // 3. 견적서로 요청 조회
                            Request request = getRequestByQuotation(quotation);
                            // 4. 요청으로 견적 요청서 조회
                            QuotationReq quotationReq = getQuotationReqByRequest(request);
                            // 5. 견적 요청서로 반려견 조회
                            Pet pet = getPetByQuotationReq(quotationReq);
                            // 6. DTO 변환
                            return groomerHomeMapper.toTodayScheduleResponse(
                                    pet, quotation, groomer);
                        })
                .collect(Collectors.toList());
    }

    // 미용 완료 여부 수정
    public void updateComplete(
            Long quotationId, QuotationUpdateCompleteRequest quotationUpdateCompleteRequest) {
        quotationService.updateComplete(quotationId, quotationUpdateCompleteRequest);
    }

    // 받은 견적요청서 리스트 조회
    public List<HomeQuotationReqResponse> getRequestList(Long shopId) {
        getShop(shopId);
        // 1. 매장으로 받은 미승인 요청 리스트 조회
        List<Request> requestList = requestService.getNewRequestsByShopId(shopId);
        if (requestList.isEmpty()) { // 해당 요청 리스트 없음
            return Collections.emptyList();
        }
        return requestList.stream()
                .map(
                        request -> {
                            // 2. 요청으로 견적 요청서 조회
                            QuotationReq quotationReq = getQuotationReqByRequest(request);
                            // 3. 견적 요청서로 반려견 조회
                            Pet pet = getPetByQuotationReq(quotationReq);
                            return groomerHomeMapper.toHomeQuotationReqResponse(
                                    request, quotationReq, pet);
                        })
                .collect(Collectors.toList());
    }
}
