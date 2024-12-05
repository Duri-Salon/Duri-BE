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

    private Long userId; // 로그인한 고객 ID
    private Long shopId; // 리뷰할 매장 ID
    private Long requestId; // 리뷰를 남길 시술(요청) ID
    private Integer rating; // 별점
    private String comment; // 후기
}
