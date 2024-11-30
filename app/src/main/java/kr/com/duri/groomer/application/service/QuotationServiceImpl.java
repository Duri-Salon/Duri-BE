package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.exception.QuotationExistsException;
import kr.com.duri.groomer.exception.QuotationNotFoundException;
import kr.com.duri.groomer.repository.GroomerRepository;
import kr.com.duri.groomer.repository.QuotationRepository;
import kr.com.duri.user.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final RequestRepository requestRepository;
    private final GroomerRepository groomerRepository;

    @Override
    public void saveQuotation(Quotation quotation) {
        boolean existsQuotation =
                quotationRepository.existsByRequestId(quotation.getRequest().getId());
        if (existsQuotation) {
            throw new QuotationExistsException("해당 요청 ID에 대한 견적이 이미 존재합니다.");
        }
        quotationRepository.save(quotation);
    }

    @Override
    public Quotation findQuotationById(Long quotationId) {
        return quotationRepository
                .findById(quotationId)
                .orElseThrow(() -> new QuotationNotFoundException("견적서를 찾을 수 없습니다."));
    }

    @Override
    public void updateQuotation(Quotation quotation) {
        quotationRepository.save(quotation);
    }

    @Override
    public Quotation findByRequestId(Long requestId) {
        return quotationRepository
                .findByRequestId(requestId)
                .orElseThrow(() -> new QuotationNotFoundException("해당 견적을 찾을 수 없습니다."));
    }
}
