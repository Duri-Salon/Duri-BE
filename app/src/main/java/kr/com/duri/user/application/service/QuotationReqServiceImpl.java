package kr.com.duri.user.application.service;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.mapper.QuotationReqMapper;
import kr.com.duri.user.domain.entity.Request;
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
