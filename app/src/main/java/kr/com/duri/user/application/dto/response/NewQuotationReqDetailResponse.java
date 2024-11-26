package kr.com.duri.user.application.dto.response;

import kr.com.duri.groomer.application.dto.response.GroomerDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewQuotationReqDetailResponse {

    private String userName; // 요청 사용자 이름
    private PetDetailResponse pet; // 반려견 정보
    private GroomerDetailResponse groomer; // 디자이너 정보
    private MenuDetailResponse quotationDetails; // 견적 요청 사항
}
