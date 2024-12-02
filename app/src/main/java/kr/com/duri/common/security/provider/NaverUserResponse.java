package kr.com.duri.common.security.provider;

import java.util.Map;

public class NaverUserResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverUserResponse(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() { // 고유 아이디
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    public String getName() {
        return attributes.get("name").toString();
    }

    public String getGender() {
        return attributes.get("gender").toString();
    }

    public String getBirthday() {
        return attributes.get("birthday").toString();
    }

    public String getMobile() { // 010-1234-5678
        return attributes.get("mobile").toString();
    }

    public String getMobileE164() { // +821012345678
        return attributes.get("mobile_e164").toString();
    }

    public String getBirthyear() {
        return attributes.get("birthyear").toString();
    }
}
