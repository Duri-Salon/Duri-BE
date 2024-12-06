package kr.com.duri.groomer.application.service;

import java.util.List;

import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.domain.entity.Quotation;

public interface QuotationService {

    void saveQuotation(Quotation quotation);

    Quotation findQuotationById(Long quotationId);

    void updateQuotation(Quotation quotation);

    Quotation findByRequestId(Long requestId);

    // 가장 최근 시술 견적서 조회
    Quotation getClosetQuoationByShopId(Long shopId);

    // 매장의 당일 시술 견적서 조회
    List<Quotation> getTodayQuotations(Long shopId);

    // 시술 완료 여부 수정
    Quotation updateComplete(
            Long quotationId, QuotationUpdateCompleteRequest quotationUpdateCompleteRequest);

    // QuotationId로 Quotation 조회
    Quotation findById(Long quotationId);

    List<Quotation> findByQuotationReqId(Long quotationReqId);

    // 사용자의 다음 시술 견적서 조회
    Quotation getClosetQuoationByUserId(Long UserId);
}
