package kr.com.duri.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewReviewRequest {
    @Builder.Default private Integer rating = 5; // 별점
    @Builder.Default private String comment = ""; // 후기
}
