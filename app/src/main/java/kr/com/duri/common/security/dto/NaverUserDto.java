package kr.com.duri.common.security.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NaverUserDto {
    private Long id;
    private String providerId;
    private String provider;
    private String email;
    private String role;
    private String client;
    private Boolean newUser;

    public void updateId(Long id) {
        this.id = id;
    }

    public void updateNewUser(Boolean newUser) {
        this.newUser = newUser;
    }
}
