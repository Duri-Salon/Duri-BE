package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeQuotationReqResponse {
    private Long requestId; // 요청 ID
    private Long petId; // 반려견 ID
    private String imageURL; // 반려견 이미지
    private String name; // 반려견 이름
    private String breed; // 견종
    private String gender; // 성별
    private Integer age; // 나이
    private Integer weight; // 무게
    private boolean neutering; // 중성화여부
    private Long quotationReqId; // 견적 요청서 ID
    private String memo; // 요구사항
}
