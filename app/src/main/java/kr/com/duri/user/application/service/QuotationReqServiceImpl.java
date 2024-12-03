package kr.com.duri.user.application.service;

import kr.com.duri.user.domain.entity.QuotationReq;
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
}
