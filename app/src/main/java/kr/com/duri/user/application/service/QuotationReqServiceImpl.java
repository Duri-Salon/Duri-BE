package kr.com.duri.user.application.service;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.exception.GroomerNotFoundException;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.groomer.repository.GroomerRepository;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.mapper.QuotationReqMapper;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.exception.RequestNotFoundException;
import kr.com.duri.user.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationReqServiceImpl implements QuotationReqService {

    private final RequestRepository requestRepository;
    private final QuotationReqMapper quotationMapper;
    private final GroomerRepository groomerRepository;

    @Override
    public List<NewQuotationReqResponse> getNewRequests(Long shopId) {
        List<Request> requests = requestRepository.findNewRequestsByShopId(shopId);
        if (requests.isEmpty()) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }
        return requests.stream()
                .map(quotationMapper::toNewQuotationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NewQuotationReqDetailResponse getQuotationReqDetail(Long requestId) {
        // 견적 요청 ID로 데이터를 조회
        Request request =
                requestRepository
                        .findById(requestId)
                        .orElseThrow(() -> new RequestNotFoundException("견적 요청을 찾을 수 없습니다."));

        Long shopId = request.getShop().getId();

        // shopId로 groomer찾기
        Groomer groomer =
                groomerRepository.findByShopId(shopId).stream()
                        .findFirst() // 여러 명의 미용사가 있을 수 있으므로 첫 번째 미용사를 선택
                        .orElseThrow(() -> new GroomerNotFoundException("해당 매장의 미용사를 찾을 수 없습니다."));

        // 엔티티를 DTO로 변환
        return quotationMapper.toQuotationReqDetailResponse(request, groomer);
    }
}
