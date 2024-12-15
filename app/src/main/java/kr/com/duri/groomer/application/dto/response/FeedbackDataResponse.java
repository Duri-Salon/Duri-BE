package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDataResponse {
    private String friendly; // 미용사와의 친화력
    private String reaction; // 미용도구 반응
    private String behavior; // 반려견 행동
}
