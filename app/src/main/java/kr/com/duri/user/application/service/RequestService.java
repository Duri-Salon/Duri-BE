package kr.com.duri.user.application.service;

import kr.com.duri.user.domain.entity.Request;

import java.util.List;

public interface RequestService {
    Request getRequestById(Long requestId);
    List<Request> getNewRequestsByShopId(Long shopId);
}
