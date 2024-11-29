package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.user.domain.entity.Request;

public interface QuotationReqService {
    Request getRequestById(Long requestId);

    List<Request> getNewRequestsByShopId(Long shopId);

    void updateRequestStatusToApproved(Long requestId);
}
