package kr.com.duri.user.application.mapper;

import java.sql.Time;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.response.GroomerDetailResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.dto.request.NewQuotationReqRequest;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.domain.Enum.QuotationStatus;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.QuotationReq;
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
                        .day(request.getQuotation().getDay())
                        .time9(request.getQuotation().getTime9())
                        .time10(request.getQuotation().getTime10()) //
                        .time11(request.getQuotation().getTime11())
                        .time12(request.getQuotation().getTime12())
                        .time13(request.getQuotation().getTime13())
                        .time14(request.getQuotation().getTime14())
                        .time15(request.getQuotation().getTime15())
                        .time16(request.getQuotation().getTime16())
                        .time17(request.getQuotation().getTime17())
                        .time18(request.getQuotation().getTime18())
                        .build();

        // 최종 DTO 반환
        return NewQuotationReqDetailResponse.builder()
                .userName(pet.getUser().getName()) // 요청 사용자 이름
                .userPhone(pet.getUser().getPhone()) // 요청 사용자 전화번호
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
                .petWeight(pet.getWeight()) // 강아지 무게
                .petNeutering(pet.getNeutering()) // 특이사항1 - 강아지 중성화 여부
                .petCharacter(parseJsonArray(pet.getCharacter())) // 강아지 성격 정보
                .petDiseases(parseJsonArray(pet.getDiseases())) // 강아지 질환 정보
                .totalPrice(totalPrice)
                .status(String.valueOf(quotation.getStatus()))
                .build();
    }

    public ReservationQuotationReqResponse toReservationQuotationResponse(Quotation quotation) {
        Integer totalPrice = extractTotalPriceFromJson(quotation.getPrice());
        Pet pet = quotation.getRequest().getQuotation().getPet();
        LocalDate today = LocalDate.now(); // 현재 날짜
        LocalDate date = quotation.getStartDateTime().toLocalDate(); //미용 날짜

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

        return ReservationQuotationReqResponse.builder()
                .requestId(quotation.getRequest().getId()) // 요청 ID
                .userId(pet.getUser().getId()) // user ID
                .petId(pet.getId()) // 강아지 ID
                .petDetailResponse(petDetailResponse)
                .totalPrice(totalPrice)
                .dday((int) ChronoUnit.DAYS.between(today, date))
                .date(date)
                .startTime(Time.valueOf(quotation.getStartDateTime().toLocalTime()))
                .endTime(Time.valueOf(quotation.getEndDateTime().toLocalTime()))
                .build();
    }

    public QuotationReq toQuotationReqEntity(
            NewQuotationReqRequest newQuotationReqRequest, Pet pet) {
        return QuotationReq.builder()
                .pet(pet) // petId
                .close(false) // 마감여부는 false로 기본 세팅
                .maxPrice(newQuotationReqRequest.getMaxPrice()) // 희망 최대금액
                .menu(newQuotationReqRequest.getMenu()) // 기본 미용메뉴
                .addMenu(newQuotationReqRequest.getAddMenu()) // 추가 미용메뉴
                .specialMenu(newQuotationReqRequest.getSpecialMenu()) // 스페셜 미용 메뉴
                .design(newQuotationReqRequest.getDesign()) // 디자인 컷
                .petSize(newQuotationReqRequest.getPetSize()) // 반려견 품종(소형/중형/대형)
                .etc(newQuotationReqRequest.getEtc()) // 기타 요구사항
                .day(newQuotationReqRequest.getDay()) // 미용희망 날짜
                .time9(newQuotationReqRequest.getTime9()) // 9시 가능여부
                .time10(newQuotationReqRequest.getTime10()) // 10시 가능여부
                .time11(newQuotationReqRequest.getTime11()) // 11시 가능여부
                .time12(newQuotationReqRequest.getTime12()) // 12시 가능여부
                .time13(newQuotationReqRequest.getTime13()) // 13시 가능여부
                .time14(newQuotationReqRequest.getTime14()) // 14시 가능여부
                .time15(newQuotationReqRequest.getTime15()) // 15시 가능여부
                .time16(newQuotationReqRequest.getTime16()) // 16시 가능여부
                .time17(newQuotationReqRequest.getTime17()) // 17시 가능여부
                .time18(newQuotationReqRequest.getTime18()) // 18시 가능여부
                .build();
    }

    public List<Request> toRequestEntities(QuotationReq quotationReq, List<Long> shopIds) {
        return shopIds.stream()
                .map(
                        shopId ->
                                Request.builder()
                                        .quotation(quotationReq) // 견적 요청서 저장
                                        .shop(Shop.builder().id(shopId).build()) // 미용 매장ID저장
                                        .status(QuotationStatus.WAITING) // 상태는 대기로 세팅
                                        .build())
                .collect(Collectors.toList());
    }
}
