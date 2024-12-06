package kr.com.duri.user.application.service.impl;

import java.util.List;

import kr.com.duri.user.application.service.RequestService;
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
}
