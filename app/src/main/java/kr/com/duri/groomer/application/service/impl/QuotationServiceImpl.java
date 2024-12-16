package kr.com.duri.groomer.application.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateNoshowRequest;
import kr.com.duri.groomer.application.service.QuotationService;
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

        // 존재하는 견적이지만, 동일 ID인 경우 업데이트 허용
        if (existsQuotation && !quotationRepository.existsById(quotation.getId())) {
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

    // 견적서 찾는 용(예외 미처리)
    @Override
    public Quotation getByRequestId(Long requestId) {
        return quotationRepository.findByRequestId(requestId).orElse(null);
    }

    // 가장 최근 시술 견적서 조회
    @Override
    public Quotation getClosetQuoationByShopId(Long shopId) {
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
        quotation.updateComplete(quotationUpdateCompleteRequest.getComplete());
        return quotationRepository.save(quotation);
    }

    // 노쇼 여부 업데이트
    @Override
    public Quotation updateNoshow(
            Long quotationId, QuotationUpdateNoshowRequest quotationUpdateNoshowRequest) {
        Quotation quotation = findQuotationById(quotationId);
        quotation.updateNoShow(quotationUpdateNoshowRequest.getNoshow());
        return quotationRepository.save(quotation);
    }

    // QuotationId로 Quotation 조회
    @Override
    public Quotation findById(Long quotationId) {
        return quotationRepository
                .findById(quotationId)
                .orElseThrow(() -> new QuotationNotFoundException("해당 견적을 찾을 수 없습니다."));
    }

    @Override
    public List<Quotation> findByQuotationReqId(Long quotationReqId) {
        return quotationRepository.findByRequest_Quotation_Id(quotationReqId);
    }

    // 사용자의 다음 시술 견적서 조회
    @Override
    public Quotation getClosetQuoationByUserId(Long userId) {
        Optional<Quotation> quotation =
                quotationRepository.findApprovedNextQuotation(userId, LocalDateTime.now());
        if (!quotation.isPresent()) { // 조회된 견적서 없음
            return null;
        }
        return quotation.get();
    }

    // 반려견 ID로 사용자의 견적서 개수 조회 : [매장 ID, 방문횟수]
    @Override
    public List<Object[]> getRegularInfoByPetId(Long petId) {
        List<Object[]> quotationList = quotationRepository.findRegularShop(petId);
        if (quotationList.isEmpty()) { // 조회된 견적서 없음
            return Collections.emptyList();
        }
        return quotationList;
    }

    @Override
    public List<Quotation> getHistoryByPetId(Long petId) {
        List<Quotation> quotationList = quotationRepository.findQuotationsByPetId(petId);
        if (quotationList.isEmpty()) {
            return Collections.emptyList();
        }
        return quotationList;
    }

    @Override
    public List<Quotation> findByRequestIdsOrderByPrice(List<Long> requestIds) {
        return quotationRepository.findByRequestIdInOrderByPriceAsc(requestIds);
    }

    @Override
    public List<Quotation> getNoShowHistoryByPetId(Long petId) {
        List<Quotation> quotationList = quotationRepository.findNoShowQuotationsByPetId(petId);
        if (quotationList.isEmpty()) {
            return Collections.emptyList();
        }
        return quotationList;
    }

    @Override
    public List<Quotation> getApprovedHistoryByPetId(Long petId) {
        List<Quotation> quotationList = quotationRepository.findApprovedQuotationsByPetId(petId);
        if (quotationList.isEmpty()) {
            return Collections.emptyList();
        }
        return quotationList;
    }
}
