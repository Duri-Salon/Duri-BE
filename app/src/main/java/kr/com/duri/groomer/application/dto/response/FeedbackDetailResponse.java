package kr.com.duri.groomer.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDetailResponse {
    private Long feedbackId; // 피드백 ID
    private String friendly; // 미용사와의 친화력
    private String reaction; // 미용도구 반응
    private String matter; // 스트레스 및 질환 여부
    private String behavior; // 반려견 행동
    private String noticeContent; // 사용자에게 전달되는 내용
    private String portfolioContent; // 포트폴리오 문구
    private Boolean expose; // 포트폴리오 노출 여부 (T: 노출, F: 비노출)
    private Boolean deleted; // 삭제 여부
    private List<String> feedbackImages; // 피드백 이미지 URL
}
