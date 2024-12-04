package kr.com.duri.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewJwtResponse {
    private String client;
    private String token;
    private Boolean newUser;
}
