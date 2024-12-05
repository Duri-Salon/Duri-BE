package kr.com.duri.groomer.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewShopJwtResponse {
    private String client;
    private String token;
    private Boolean newUser;
}
