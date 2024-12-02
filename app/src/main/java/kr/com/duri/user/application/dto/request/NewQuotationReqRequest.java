package kr.com.duri.user.application.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewQuotationReqRequest {
    private Long petId; // 반려견 ID
    private Integer maxPrice; // 최대 금액
    private String menu; // 미용 메뉴
    private String addMenu; // 추가 미용 메뉴
    private String specialMenu; // 스페셜 케어
    private String design; // 디자인 컷
    private String petSize; // 반려견 품종(소형/중형/대형)
    private String etc; // 기타 요구사항
    private LocalDate day; // 예약일
    private Boolean time9;
    private Boolean time10;
    private Boolean time11;
    private Boolean time12;
    private Boolean time13;
    private Boolean time14;
    private Boolean time15;
    private Boolean time16;
    private Boolean time17;
    private Boolean time18;
    private List<Long> shopIds; // 매장 ID 리스트
}
