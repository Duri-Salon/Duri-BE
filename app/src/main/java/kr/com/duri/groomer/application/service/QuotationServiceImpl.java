package kr.com.duri.groomer.application.service;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.quotations.response.NewQuotationResponse;
import kr.com.duri.groomer.application.mapper.QuotationMapper;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final RequestRepository requestRepository;
    private final QuotationMapper quotationMapper;

    @Override
    public List<NewQuotationResponse> getNewRequests(Long shopId) {
        List<Request> requests = requestRepository.findNewRequestsByShopId(shopId);
        if (requests.isEmpty()) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }
        return requests.stream()
                .map(quotationMapper::toNewQuotationResponse)
                .collect(Collectors.toList());
    }
}
