package kr.com.duri.user.application.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewQuotationReqResponse {
    private Long requestId; // 요청 ID
    private Long userId; // 고객 ID
    private Long petId; // 애완견 ID
    private String petImage; // 강아지 이미지
    private String petName; // 강아지 이름
    private Integer petAge; // 강아지 나이
    private String petBreed; // 견종
    private Boolean petNeutering; // 특이사항1 - 중성화여부
    private Boolean petBiting; // 특이사항2 - 입질여부
    private LocalDateTime requestCreatedAt; // 견적 요청 날짜
}
