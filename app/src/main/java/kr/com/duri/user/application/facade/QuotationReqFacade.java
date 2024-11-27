package kr.com.duri.user.application.facade;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.mapper.QuotationReqMapper;
import kr.com.duri.user.application.service.QuotationReqService;
import kr.com.duri.user.application.service.RequestService;
import kr.com.duri.user.domain.entity.Request;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationReqFacade {

    private final RequestService requestService;
    private final GroomerService groomerService;
    private final QuotationReqService quotationReqService;
    private final QuotationReqMapper quotationMapper;

    // 새로운 견적 요청서 리스트
    public List<NewQuotationReqResponse> getNewRequests(Long shopId) {
        List<Request> requests = requestService.getNewRequestsByShopId(shopId);
        if (requests.isEmpty()) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }
        return requests.stream()
                .map(quotationMapper::toNewQuotationResponse)
                .collect(Collectors.toList());
    }

    // 견적 요청 상세 정보
    public NewQuotationReqDetailResponse getQuotationReqDetail(Long requestId) {
        // 견적 요청 ID로 데이터를 조회
        Request request = requestService.getRequestById(requestId);
        Long shopId = request.getShop().getId();

        // shopId로 groomer찾기
        Groomer groomer = groomerService.getGroomerByShopId(shopId);

        // 엔티티를 DTO로 변환
        return quotationReqService.getQuotationReqDetail(request, groomer);
    }
}

