package kr.com.duri.user.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LastMenuDetailResponse {

    private List<String> groomingMenu; // 미용 메뉴
    private List<String> additionalGrooming; // 추가 미용
    private List<String> specialCare; // 스페셜 케어
    private List<String> designCut; // 디자인 컷
    private String otherRequests; // 기타 요청 사항
}
