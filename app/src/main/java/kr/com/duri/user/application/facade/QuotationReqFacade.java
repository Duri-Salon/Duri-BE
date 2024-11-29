package kr.com.duri.user.application.facade;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.response.ApprovedQuotationReqResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.mapper.QuotationReqMapper;
import kr.com.duri.user.application.service.QuotationReqService;
import kr.com.duri.user.domain.entity.Request;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationReqFacade {

    private final QuotationReqService quotationReqService;
    private final GroomerService groomerService;
    private final QuotationReqMapper quotationReqMapper;

    // 새로운 견적 요청서 리스트
    public List<NewQuotationReqResponse> getNewRequests(Long shopId) {
        // 1. shopId 유효성 확인
        boolean shopExists = groomerService.existsByShopId(shopId);
        if (!shopExists) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }

        // 2. WAITING 상태의 요청 조회
        List<Request> requests = quotationReqService.getNewRequestsByShopId(shopId);

        // 3. 요청 목록을 응답 DTO로 변환
        return requests.stream()
                .map(quotationReqMapper::toNewQuotationResponse)
                .collect(Collectors.toList());
    }

    // 견적 요청 상세 정보
    public NewQuotationReqDetailResponse getQuotationReqDetail(Long requestId) {
        // 견적 요청 ID로 데이터를 조회
        Request request = quotationReqService.getRequestById(requestId);
        Long shopId = request.getShop().getId();

        // shopId로 groomer찾기
        Groomer groomer = groomerService.getGroomerByShopId(shopId);

        // 3. Mapper를 사용하여 DTO 생성
        return quotationReqMapper.toQuotationReqDetailResponse(request, groomer);
    }

    // 답장한 견적 요청서 리스트
    public List<ApprovedQuotationReqResponse> getApprovedRequests(Long shopId) {
        // 1. shopId 유효성 확인
        boolean shopExists = groomerService.existsByShopId(shopId);
        if (!shopExists) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }

        // 2. APPROVED 상태의 요청 조회
        List<Request> requests = quotationReqService.getApprovedRequestsByShopId(shopId);

        // 3. 요청 목록을 응답 DTO로 변환
        return requests.stream()
                .map(
                        request -> {
                            Quotation quotation =
                                    quotationReqService.getQuotationByRequestId(request.getId());
                            return quotationReqMapper.toApprovedQuotationResponse(quotation);
                        })
                .collect(Collectors.toList());
    }
}
