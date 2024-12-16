package kr.com.duri.user.application.service.impl;

import java.util.Collections;
import java.util.List;

import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.application.service.RequestService;
import kr.com.duri.user.domain.Enum.QuotationStatus;
import kr.com.duri.user.domain.entity.QuotationReq;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.exception.RequestNotFoundException;
import kr.com.duri.user.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public void saveRequests(List<Request> requests) {
        requestRepository.saveAll(requests);
    }

    @Override
    public Request getRequestById(Long requestId) {
        return requestRepository
                .findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("견적 요청을 찾을 수 없습니다."));
    }

    @Override
    public List<Request> getNewRequestsByShopId(Long shopId) {
        return requestRepository.findNewRequestsByShopId(shopId);
    }

    @Override
    public void updateRequestStatusToApproved(Long requestId) {
        Request existingRequest = getRequestById(requestId);
        Request updatedRequest = existingRequest.withApprovedStatus();
        requestRepository.save(updatedRequest);
    }

    @Override
    public List<Request> getApprovedRequestsByShopId(Long shopId) {
        return requestRepository.findApprovedRequestsByShopId(shopId);
    }

    @Override
    public List<Request> getReservationRequestsByShopId(Long shopId) {
        return requestRepository.findReservationQuotationsByShopId(shopId);
    }

    @Override
    public List<Request> getCompleteRequestsByShopId(Long shopId) {
        return requestRepository.findCompleteQuotationsByShopId(shopId);
    }

    @Override
    public List<Request> findByQuotationId(Long quotationId) {
        return requestRepository.findByQuotationId(quotationId);
    }

    // QuotationReq와 관련된 Request들을 가져옴
    @Override
    public List<Request> findRequestsByQuotation(QuotationReq quotationReq) {
        return requestRepository.findByQuotation(quotationReq);
    }

    // Request상태를 EXPIRED로 업데이트
    @Override
    public void updateRequestStatusToExpired(Request request) {
        request.updateStatus(QuotationStatus.EXPIRED);
    }

    @Override
    public List<Request> getApprovedHistoryByShopId(Long shopId) {
        List<Request> requestList = requestRepository.findApprovedRequestsByShopId(shopId);
        if (requestList.isEmpty()) {
            return Collections.emptyList();
        }
        return requestList;
    }
}
