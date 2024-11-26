package kr.com.duri.user.application.mapper;

import kr.com.duri.groomer.application.dto.response.GroomerDetailResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.response.MenuDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.dto.response.PetDetailResponse;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;

import org.springframework.stereotype.Component;

@Component
public class QuotationReqMapper {

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
                .petBiting(pet.getBiting()) // 특이사항2 - 입질여부
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
                        .neutering(pet.getNeutering()) // 강아지 중성화 여부
                        .biting(pet.getBiting()) // 강아지 입질 여부
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
}