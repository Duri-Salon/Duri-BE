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
public class DiaryDetailResponse {
    private GroomerInfoResponse groomerInfo; // 미용사 정보

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime groomingDate; // 날짜

    private Long feedbackId; // 피드백 ID
    private String friendly; // 미용사와의 친화력
    private String reaction; // 미용도구 반응
    private String behavior; // 반려견 행동
    private String noticeContent; // 고객에게 전달되는 문구
    private List<String> feedbackImages; // 피드백 이미지 URL
}
