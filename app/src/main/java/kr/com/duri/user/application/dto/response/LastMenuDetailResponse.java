package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LastMenuDetailResponse {

    private String groomingMenu; // 미용 메뉴
    private String additionalGrooming; // 추가 미용
    private String specialCare; // 스페셜 케어
    private String designCut; // 디자인 컷
    private String otherRequests; // 기타 요청 사항
}
