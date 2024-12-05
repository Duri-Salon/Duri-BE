package kr.com.duri.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewUserJwtResponse {
    private String username;
    private String client;
    private String token;
    private Boolean newUser;
}
