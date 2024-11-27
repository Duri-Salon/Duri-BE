package kr.com.duri.user.application.service;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.exception.GroomerNotFoundException;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.groomer.repository.GroomerRepository;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.mapper.QuotationReqMapper;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.exception.RequestNotFoundException;
import kr.com.duri.user.repository.RequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationReqServiceImpl implements QuotationReqService {

    private final QuotationReqMapper quotationMapper;

    @Override
    public NewQuotationReqDetailResponse getQuotationReqDetail(Request request, Groomer groomer) {
        return quotationMapper.toQuotationReqDetailResponse(request, groomer);
    }
}
