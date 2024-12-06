package kr.com.duri.user.application.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuDetailResponse {

    private String groomingMenu; // 미용 메뉴
    private String additionalGrooming; // 추가 미용
    private String specialCare; // 스페셜 케어
    private String designCut; // 디자인 컷
    private String otherRequests; // 기타 요청 사항
    private LocalDate day; // 희망 날짜
    private Boolean time9; // 9시 희망 여부
    private Boolean time10; // 10시 희망 여부
    private Boolean time11; // 11시 희망 여부
    private Boolean time12; // 12시 희망 여부
    private Boolean time13; // 13시 희망 여부
    private Boolean time14; // 14시 희망 여부
    private Boolean time15; // 15시 희망 여부
    private Boolean time16; // 16시 희망 여부
    private Boolean time17; // 17시 희망 여부
    private Boolean time18; // 18시 희망 여부
}
