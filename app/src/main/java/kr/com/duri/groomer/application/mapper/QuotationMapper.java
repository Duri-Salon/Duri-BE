package kr.com.duri.groomer.application.mapper;

import kr.com.duri.groomer.application.dto.quotations.response.NewQuotationResponse;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;
import org.springframework.stereotype.Component;

@Component
public class QuotationMapper {

    public NewQuotationResponse toNewQuotationResponse(Request request) {
        Pet pet = request.getQuotation().getPet();

        return NewQuotationResponse.builder()
                .quotationReqId(request.getQuotation().getId()) // 견적요청서 ID
                .userId(pet.getUser().getId())                  // user ID
                .petId(pet.getId())                             // 애완견 ID
                .petImage(pet.getImage())                       // 애완견 사진
                .petName(pet.getName())                         // 애완견 이름
                .petAge(pet.getAge())                           // 애완견 나이
                .petBreed(pet.getBreed())                       // 애완견 견종
                .petNeutering(pet.getNeutering())               //특이사항1 - 중성화여부
                .petBiting(pet.getBiting())                     //특이사항2 - 입질여부
                .requestCreatedAt(request.getCreatedAt())       // 요청 생성일
                .build();
    }
}
