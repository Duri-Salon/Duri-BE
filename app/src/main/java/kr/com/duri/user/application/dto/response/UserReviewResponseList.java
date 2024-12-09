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
public class UserReviewResponseList {

    private Integer reviewCnt; // 리뷰 개수
    private List<UserReviewResponse> reviewList; // 리뷰 목록
}
