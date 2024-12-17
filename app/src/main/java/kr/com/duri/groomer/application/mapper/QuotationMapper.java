package kr.com.duri.groomer.application.mapper;

import java.util.List;

import kr.com.duri.groomer.application.dto.request.PriceDetailRequest;
import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.application.dto.response.QuotationDetailResponse;
import kr.com.duri.groomer.application.dto.response.QuotationShopDetailResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.dto.response.MenuDetailResponse;
import kr.com.duri.user.application.dto.response.PetDetailResponse;
import kr.com.duri.user.domain.Enum.QuotationStatus;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
public class QuotationMapper {

    private final ObjectMapper objectMapper;

    public QuotationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // JSON 문자열을 List<String>로 변환하는 메서드
    private List<String> parseJsonArray(String jsonString) {
        try {
            // 문자열로 저장된 JSON 배열을 List<String>으로 변환
            List<String> list =
                    objectMapper.readValue(
                            jsonString,
                            TypeFactory.defaultInstance()
                                    .constructCollectionType(List.class, String.class));
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 문자열을 리스트로 변환할 수 없습니다.", e);
        }
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
                .complete(false)
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

    public QuotationDetailResponse toQuotationDetailResponse(
            Request request,
            Shop shop,
            ShopImage shopImage,
            Groomer groomer,
            Pet pet,
            Quotation quotation,
            String status) {

        String imageURL = (shopImage != null) ? shopImage.getShopImageUrl() : null;
        // ShopDetailResponse 생성
        QuotationShopDetailResponse quotationShopDetailResponse =
                QuotationShopDetailResponse.builder()
                        .shopName(shop.getName())
                        .shopAddress(shop.getAddress())
                        .shopPhone(shop.getPhone())
                        .groomerName(groomer.getName())
                        .shopImage(imageURL)
                        .build();

        // PetDetailResponse 생성
        PetDetailResponse petDetailResponse =
                PetDetailResponse.builder()
                        .image(pet.getImage())
                        .name(pet.getName())
                        .age(pet.getAge())
                        .gender(pet.getGender())
                        .breed(pet.getBreed())
                        .weight(pet.getWeight())
                        .neutering(pet.getNeutering())
                        .character(parseJsonArray(pet.getCharacter()))
                        .diseases(parseJsonArray(pet.getDiseases()))
                        .lastGrooming(pet.getLastGrooming())
                        .build();

        // MenuDetailResponse 생성
        MenuDetailResponse menuDetailResponse =
                MenuDetailResponse.builder()
                        .groomingMenu(parseJsonArray(request.getQuotation().getMenu()))
                        .additionalGrooming(parseJsonArray(request.getQuotation().getAddMenu()))
                        .specialCare(parseJsonArray(request.getQuotation().getSpecialMenu()))
                        .designCut(parseJsonArray(request.getQuotation().getDesign()))
                        .otherRequests(request.getQuotation().getEtc())
                        .day(request.getQuotation().getDay())
                        .time9(request.getQuotation().getTime9())
                        .time10(request.getQuotation().getTime10())
                        .time11(request.getQuotation().getTime11())
                        .time12(request.getQuotation().getTime12())
                        .time13(request.getQuotation().getTime13())
                        .time14(request.getQuotation().getTime14())
                        .time15(request.getQuotation().getTime15())
                        .time16(request.getQuotation().getTime16())
                        .time17(request.getQuotation().getTime17())
                        .time18(request.getQuotation().getTime18())
                        .build();

        // QuotationRequest 생성
        QuotationRequest quotationRequest =
                QuotationRequest.builder()
                        .requestId(quotation.getRequest().getId())
                        .memo(quotation.getMemo())
                        .startDateTime(quotation.getStartDateTime())
                        .endDateTime(quotation.getEndDateTime())
                        .priceDetail(parsePriceDetail(quotation.getPrice()))
                        .build();

        // QuotationDetailResponse 통합 생성
        return QuotationDetailResponse.builder()
                .shopDetail(quotationShopDetailResponse)
                .quotationCreatedAt(quotation.getCreatedAt())
                .petDetail(petDetailResponse)
                .menuDetail(menuDetailResponse)
                .quotationId(quotation.getId())
                .quotation(quotationRequest)
                .status(status)
                .build();
    }

    // JSON 문자열을 PriceDetailRequest로 파싱
    private PriceDetailRequest parsePriceDetail(String priceJson) {
        try {
            return objectMapper.readValue(priceJson, PriceDetailRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("가격 정보 파싱 실패", e);
        }
    }
}
