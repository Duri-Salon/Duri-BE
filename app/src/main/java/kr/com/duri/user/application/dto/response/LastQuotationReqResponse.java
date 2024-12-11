package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LastQuotationReqResponse {

    private String userName; // 요청 사용자 이름
    private String userPhone; // 요청 사용자 전화번호
    private PetDetailResponse pet; // 반려견 정보
    private LastMenuDetailResponse quotationDetails; // 견적 요청 사항
}
