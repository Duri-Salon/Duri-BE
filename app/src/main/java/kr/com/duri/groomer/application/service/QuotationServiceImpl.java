package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.mapper.QuotationMapper;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.exception.QuotationExistsException;
import kr.com.duri.groomer.repository.QuotationRepository;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.exception.RequestNotFoundException;
import kr.com.duri.user.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final RequestRepository requestRepository;
    private final QuotationRepository quotationRepository;
    private final QuotationMapper quotationMapper;

    @Override
    public void saveQuotation(QuotationRequest quotationRequest) {

        Request request =
                requestRepository
                        .findById(quotationRequest.getRequestId())
                        .orElseThrow(() -> new RequestNotFoundException("해당 요청 ID를 찾을 수 없습니다."));

        boolean existsQuotation =
                quotationRepository.existsByRequestId(quotationRequest.getRequestId());
        if (existsQuotation) {
            throw new QuotationExistsException("해당 요청 ID에 대한 견적이 이미 존재합니다.");
        }

        Quotation quotation =
                quotationMapper.toQuotationEntity(
                        request, quotationRequest, quotationRequest.getPriceDetail());
        quotationRepository.save(quotation);
    }
}
