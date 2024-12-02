package kr.com.duri.common.security.provider;

import java.util.Map;

public class NaverShopResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverShopResponse(Map<String, Object> attributes) {
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
}
