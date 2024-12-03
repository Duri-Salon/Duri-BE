package kr.com.duri.user.application.service;

import kr.com.duri.user.domain.entity.QuotationReq;
import kr.com.duri.user.exception.QuotationReqNotFoundException;
import kr.com.duri.user.repository.QuotationReqRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationReqServiceImpl implements QuotationReqService {

    private final QuotationReqRepository quotationReqRepository;

    @Override
    public QuotationReq saveQuotationRequest(QuotationReq quotationReq) {
        return quotationReqRepository.save(quotationReq);
    }

    @Override
    public QuotationReq getQuotationReqById(Long quotationReqId) {
        return quotationReqRepository
                .findById(quotationReqId)
                .orElseThrow(() -> new QuotationReqNotFoundException("해당 견적요청서를 찾을 수 없습니다."));
    }
}
