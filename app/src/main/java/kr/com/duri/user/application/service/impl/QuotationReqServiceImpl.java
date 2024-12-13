package kr.com.duri.user.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import kr.com.duri.user.application.service.QuotationReqService;
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

    // TODO : 지워도 되는지 확인
    @Override
    public QuotationReq getQuotationReqById(Long quotationReqId) {
        return quotationReqRepository
                .findById(quotationReqId)
                .orElseThrow(() -> new QuotationReqNotFoundException("해당 견적요청서를 찾을 수 없습니다."));
    }

    @Override
    public List<QuotationReq> findByPetId(Long petId) {
        return quotationReqRepository.findByPetId(petId);
    }

    @Override
    public QuotationReq findLatestByPetId(Long petId) {
        return quotationReqRepository
                .findTopByPetIdOrderByCreatedAtDesc(petId)
                .orElse(null); // 데이터가 없으면 null 반환
    }

    // 24시간 지난 요청서 중 close가 false인 것들을 찾는 메서드
    @Override
    public List<QuotationReq> findExpiredQuotationReqs(LocalDateTime thresholdTime) {
        return quotationReqRepository.findByCreatedAtBeforeAndCloseIsFalse(thresholdTime);
    }

    // close를 true로 업데이트
    @Override
    public void closeQuotationReq(QuotationReq quotationReq) {
        quotationReq.updateCloseStatus(true);
    }
}
