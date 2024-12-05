package kr.com.duri.groomer.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.exception.QuotationExistsException;
import kr.com.duri.groomer.exception.QuotationNotFoundException;
import kr.com.duri.groomer.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;

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

    // 가장 최근 시술 견적서 조회
    @Override
    public Quotation getClosetQuoation(Long shopId) {
        Optional<Quotation> quotation =
                quotationRepository.findApprovedClosetQuotation(shopId, LocalDateTime.now());
        if (!quotation.isPresent()) { // 조회된 견적서 없음
            return null;
        }
        return quotation.get();
    }

    // 매장의 당일 시술 견적서 조회
    @Override
    public List<Quotation> getTodayQuotations(Long shopId) {
        List<Quotation> quotationList =
                quotationRepository.findTodayQuotation(shopId, LocalDateTime.now());
        return quotationList;
    }

    // 시술 완료 여부 업데이트
    public Quotation updateComplete(
            Long quotationId, QuotationUpdateCompleteRequest quotationUpdateCompleteRequest) {
        Quotation quotation = findQuotationById(quotationId);
        quotation.updateComplete(quotationUpdateCompleteRequest.isComplete());
        return quotationRepository.save(quotation);
    }

    // QuotationId로 Quotation 조회
    @Override
    public Quotation findById(Long quotationId) {
        return quotationRepository.findById(quotationId).orElse(null);
    }
}
