package kr.com.duri.common.security.provider;

public interface OAuth2Response {
    // 제공자 (EX: naver, kakao)
    String getProvider();

    // 제공자에게 발급해주는 아이디(번호)
    String getProviderId();

    // 이메일
    String getEmail();

}
