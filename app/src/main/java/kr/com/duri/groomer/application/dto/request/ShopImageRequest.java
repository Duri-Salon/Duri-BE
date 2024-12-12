package kr.com.duri.groomer.application.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopImageRequest {
    private String image;
    private String category;
}
