package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUserProfileResponse {
    private String name;
    private String email;
    private String phone;
    private String profileImg;
    private Integer reservationCount;
    private Integer noShowCount;
    private Integer stamp;
}
