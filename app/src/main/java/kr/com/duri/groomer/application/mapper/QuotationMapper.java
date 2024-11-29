package kr.com.duri.groomer.application.mapper;

import kr.com.duri.groomer.application.dto.request.PriceDetailRequest;
import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.domain.Enum.QuotationStatus;
import kr.com.duri.user.domain.entity.Request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class QuotationMapper {

    private final ObjectMapper objectMapper;

    public QuotationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toPriceJson(PriceDetailRequest priceDetail) {
        try {
            return objectMapper.writeValueAsString(priceDetail);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("가격 정보를 저장할 수 없습니다.", e);
        }
    }

    public Quotation toQuotationEntity(
            Request request, QuotationRequest quotationRequest, PriceDetailRequest priceDetail) {
        String priceJson = toPriceJson(priceDetail); // JSON 변환 호출
        return Quotation.builder()
                .request(request)
                .price(priceJson)
                .memo(quotationRequest.getMemo())
                .status(QuotationStatus.WAITING)
                .startDateTime(quotationRequest.getStartDateTime())
                .endDateTime(quotationRequest.getEndDateTime())
                .build();
    }

    public void updateQuotationEntity(
            Quotation existingQuotation, QuotationUpdateRequest quotationUpdateRequest) {

        String priceJson = toPriceJson(quotationUpdateRequest.getPriceDetail());

        // 기존 엔티티에 수정 데이터 반영
        existingQuotation.update(
                priceJson,
                quotationUpdateRequest.getMemo(),
                quotationUpdateRequest.getStartDateTime(),
                quotationUpdateRequest.getEndDateTime());
    }
}
