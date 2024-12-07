package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.user.domain.entity.Request;

public interface RequestService {
    void saveRequests(List<Request> requests);

    Request getRequestById(Long requestId);

    List<Request> getNewRequestsByShopId(Long shopId);

    void updateRequestStatusToApproved(Long requestId);

    List<Request> getApprovedRequestsByShopId(Long shopId);

    List<Request> getReservationRequestsByShopId(Long shopId);

    List<Request> getCompleteRequestsByShopId(Long shopId);

    List<Request> findByQuotationId(Long quotationId);
}
