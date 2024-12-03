package kr.com.duri.common.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Value("${client.front.dev.url}")
    private String FRONTEND_DEV_URL;

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
                        "Set-Cookie", "Authorization_USER", "Authorization_SHOP", "Content-Type")
                .allowedHeaders("Authorization_USER", "Authorization_SHOP", "Content-Type")
                .allowedOrigins(FRONTEND_DEV_URL, CLIENT_USER_URL, CLIENT_SHOP_URL, CLIENT_USER_DEV_URL, CLIENT_SHOP_DEV_URL);
    }
}
