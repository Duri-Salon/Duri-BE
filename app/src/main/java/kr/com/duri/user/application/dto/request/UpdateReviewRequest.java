package kr.com.duri.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateReviewRequest {

    private Integer rating; // 별점
    private String comment; // 후기
    /* private MultipartFile img; // 이미지

    // 이미지 등록
    public void newImg(MultipartFile img) {
      this.img = img;
    } */
}
