package kr.com.duri.groomer.application.service;

import java.util.Collection;
import java.util.List;

import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateNoshowRequest;
import kr.com.duri.groomer.domain.entity.Quotation;

public interface QuotationService {

    void saveQuotation(Quotation quotation);

    Quotation findQuotationById(Long quotationId);

    void updateQuotation(Quotation quotation);

    Quotation findByRequestId(Long requestId);

    // 견적서 찾는 용(예외 미처리)
    Quotation getByRequestId(Long requestId);

    // 가장 최근 시술 견적서 조회
    Quotation getClosetQuoationByShopId(Long shopId);

    // 매장의 당일 시술 견적서 조회
    List<Quotation> getTodayQuotations(Long shopId);

    // 시술 완료 여부 수정
    Quotation updateComplete(
            Long quotationId, QuotationUpdateCompleteRequest quotationUpdateCompleteRequest);

    // 노쇼 여부 수정
    Quotation updateNoshow(
            Long quotationId, QuotationUpdateNoshowRequest quotationUpdateNoshowRequest);

    // QuotationId로 Quotation 조회
    Quotation findById(Long quotationId);

    List<Quotation> findByQuotationReqId(Long quotationReqId);

    // 사용자의 다음 시술 견적서 조회
    Quotation getClosetQuoationByUserId(Long userId);

    // 반려견 ID로 사용자의 견적서 개수 조회 : [매장 ID, 방문횟수]
    List<Object[]> getRegularInfoByPetId(Long petId);

    // 사용자 ID로 견적서 조회
    List<Quotation> getHistoryByPetId(Long petId);

    List<Quotation> findByRequestIdsOrderByPrice(List<Long> requestIds);

    List<Quotation> getNoShowHistoryByPetId(Long petId);

    List<Quotation> getApprovedHistoryByPetId(Long petId);

    List<Quotation> getNoShowHistoryByShopId(Long shopId);
}
