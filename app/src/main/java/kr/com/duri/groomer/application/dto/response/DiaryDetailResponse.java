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
public class DiaryDetailResponse {
    private Long feedbackId; // 피드백 ID
    private String friendly; // 미용사와의 친화력
    private String reaction; // 미용도구 반응
    private String matter; // 스트레스 및 질환 여부
    private String behavior; // 반려견 행동
    private String noticeContent; // 고객에게 전달되는 문구
    private List<String> feedbackImages; // 피드백 이미지 URL
}
