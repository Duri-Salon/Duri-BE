package kr.com.duri.groomer.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioDetailResponse {
    private Long feedbackId; // 피드백 ID
    private String friendly; // 미용사와의 친화력
    private String reaction; // 미용도구 반응
    private String behavior; // 반려견 행동
    private String portfolioContent; // 포트폴리오 문구
    private List<String> feedbackImages; // 피드백 이미지 URL

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime feedbackDate; // 피드백 작성일

    private PetInfoResponse petInfo; // 반려견 정보

    private GroomerInfoResponse groomerInfo; // 미용사 정보
}
