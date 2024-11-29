package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.application.mapper.QuotationMapper;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.exception.QuotationExistsException;
import kr.com.duri.groomer.exception.QuotationNotFoundException;
import kr.com.duri.groomer.repository.QuotationRepository;
import kr.com.duri.user.domain.entity.Request;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationMapper quotationMapper;

    @Override
    public void saveQuotation(Request request, QuotationRequest quotationRequest) {

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

    @Override
    public void updateQuotation(Long quotationId, QuotationUpdateRequest quotationUpdateRequest) {
        Quotation existsQuotation =
                quotationRepository
                        .findById(quotationId)
                        .orElseThrow(() -> new QuotationNotFoundException("견적서를 찾을 수 없습니다."));

        quotationMapper.updateQuotationEntity(existsQuotation, quotationUpdateRequest);

        quotationRepository.save(existsQuotation);
    }
}
