package kr.com.duri.common.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Value("${client.local.user.dev.url}")
    private String LOCAL_USER_DEV_URL;

    @Value("${client.local.shop.dev.url}")
    private String LOCAL_SHOP_DEV_URL;

    @Value("${client.user.url}")
    private String CLIENT_USER_URL;

    @Value("${client.shop.url}")
    private String CLIENT_SHOP_URL;

    @Value("${client.user.dev.url}")
    private String CLIENT_USER_DEV_URL;

    @Value("${client.shop.dev.url}")
    private String CLIENT_SHOP_DEV_URL;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
                .addMapping("/**")
                .allowedMethods("*")
                .exposedHeaders(
                        "Set-Cookie", "authorization_user", "authorization_shop", "Content-Type")
                .allowedHeaders("authorization_user", "authorization_shop", "Content-Type")
                .allowedOrigins(
                        LOCAL_USER_DEV_URL,
                        LOCAL_SHOP_DEV_URL,
                        CLIENT_USER_URL,
                        CLIENT_SHOP_URL,
                        CLIENT_USER_DEV_URL,
                        CLIENT_SHOP_DEV_URL)
                .allowCredentials(true); // 인증 정보 허용
    }
}
