package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.user.domain.entity.Request;
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
}
