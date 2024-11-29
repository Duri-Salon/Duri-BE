package kr.com.duri.user.application.mapper;

import java.util.List;

import kr.com.duri.groomer.application.dto.response.GroomerDetailResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
public class QuotationReqMapper {

    private final ObjectMapper objectMapper;

    public QuotationReqMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // JSON 문자열을 List<String>로 변환하는 메서드
    private String parseJsonArray(String jsonString) {
        try {
            // 문자열로 저장된 JSON 배열을 List<String>으로 변환
            List<String> list =
                    objectMapper.readValue(
                            jsonString,
                            TypeFactory.defaultInstance()
                                    .constructCollectionType(List.class, String.class));
            // 다시 JSON 형식의 문자열로 변환하여 리턴
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 문자열을 리스트로 변환할 수 없습니다.", e);
        }
    }

    // price에서 최종금액만 뽑아내기
    private Integer extractTotalPriceFromJson(String priceJson) {
        try {
            JsonNode jsonNode = objectMapper.readTree(priceJson);
            return jsonNode.path("totalPrice").asInt(); // "totalPrice" 추출
        } catch (JsonProcessingException e) {
            throw new RuntimeException("가격 정보를 파싱할 수 없습니다.", e);
        }
    }

    public NewQuotationReqResponse toNewQuotationResponse(Request request) {
        Pet pet = request.getQuotation().getPet();

        return NewQuotationReqResponse.builder()
                .requestId(request.getId()) // 요청 ID
                .userId(pet.getUser().getId()) // user ID
                .petId(pet.getId()) // 애완견 ID
                .petImage(pet.getImage()) // 애완견 사진
                .petName(pet.getName()) // 애완견 이름
                .petAge(pet.getAge()) // 애완견 나이
                .petBreed(pet.getBreed()) // 애완견 견종
                .petNeutering(pet.getNeutering()) // 특이사항1 - 중성화여부
                .petCharacter(parseJsonArray(pet.getCharacter())) // 특이사항2 - 성격
                .petDiseases(parseJsonArray(pet.getDiseases())) // 특이사항3 - 질환 정보
                .requestCreatedAt(request.getCreatedAt()) // 요청 생성일
                .build();
    }

    public NewQuotationReqDetailResponse toQuotationReqDetailResponse(
            Request request, Groomer groomer) {
        Pet pet = request.getQuotation().getPet();

        // 반려견 상세 정보 매핑
        PetDetailResponse petDetailResponse =
                PetDetailResponse.builder()
                        .image(pet.getImage()) // 강아지 이미지
                        .name(pet.getName()) // 강아지 이름
                        .age(pet.getAge()) // 강아지 나이
                        .gender(pet.getGender()) // 강아지 성별
                        .breed(pet.getBreed()) // 강아지 견종
                        .weight(pet.getWeight()) // 강아지 몸무게
                        .neutering(pet.getNeutering()) // 강아지 중성화
                        .character(parseJsonArray(pet.getCharacter())) // 강아지 성격 정보
                        .diseases(parseJsonArray(pet.getDiseases())) // 강아지 질환 정보
                        .lastGrooming(pet.getLastGrooming()) // 강아지 마지막 미용일자
                        .build();

        // 디자이너 정보 매핑
        GroomerDetailResponse groomerDetailResponse =
                GroomerDetailResponse.builder()
                        .image(groomer.getImage()) // 미용사 이미지
                        .name(groomer.getName()) // 미용사 이름
                        .history(groomer.getHistory()) // 미용사 경력
                        .info(groomer.getInfo()) // 미용사 자기소개
                        .build();

        // 견적 요청 사항 매핑
        MenuDetailResponse quotationDetailResponse =
                MenuDetailResponse.builder()
                        .groomingMenu(request.getQuotation().getMenu()) // 미용 메뉴
                        .additionalGrooming(request.getQuotation().getAddMenu()) // 추가 미용 메뉴
                        .specialCare(request.getQuotation().getSpecialMenu()) // 스페셜케어
                        .designCut(request.getQuotation().getDesign()) // 디자인컷
                        .otherRequests(request.getQuotation().getEtc()) // 기타 요구사항
                        .build();

        // 최종 DTO 반환
        return NewQuotationReqDetailResponse.builder()
                .userName(pet.getUser().getName()) // 요청 사용자 이름
                .pet(petDetailResponse) // 반려견 정보
                .groomer(groomerDetailResponse) // 디자이너 정보
                .quotationDetails(quotationDetailResponse) // 견적 요청 사항
                .build();
    }

    public ApprovedQuotationReqResponse toApprovedQuotationResponse(Quotation quotation) {
        Integer totalPrice = extractTotalPriceFromJson(quotation.getPrice());
        Pet pet = quotation.getRequest().getQuotation().getPet();

        return ApprovedQuotationReqResponse.builder()
                .requestId(quotation.getRequest().getId()) // 요청 ID
                .userId(pet.getUser().getId()) // user ID
                .petId(pet.getId()) // 강아지 ID
                .petImage(pet.getImage()) // 강아지 이미지
                .petName(pet.getName()) // 강아지 이름
                .petAge(pet.getAge()) // 강아지 나이
                .petBreed(pet.getBreed()) // 강아지 견종
                .petNeutering(pet.getNeutering()) // 특이사항1 - 강아지 중성화 여부
                .petCharacter(pet.getCharacter()) // 특이사항2 - 강아지 성격 정보
                .petDiseases(pet.getDiseases()) // 특이사항3 - 강아지 질환 정보
                .totalPrice(totalPrice)
                .build();
    }
}
