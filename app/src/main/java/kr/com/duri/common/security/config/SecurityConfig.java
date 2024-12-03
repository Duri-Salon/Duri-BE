package kr.com.duri.common.security.config;

import java.util.Arrays;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;
import kr.com.duri.common.security.jwt.JwtFilter;
import kr.com.duri.common.security.jwt.JwtUtil;
import kr.com.duri.common.security.oauth2.CustomOAuth2UserService;
import kr.com.duri.common.security.oauth2.CustomSuccessHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtUtil jwtUtil;

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

    public SecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            CustomSuccessHandler customSuccessHandler,
            JwtUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(
                corsCustomizer ->
                        corsCustomizer.configurationSource(
                                new CorsConfigurationSource() {

                                    @Override
                                    public CorsConfiguration getCorsConfiguration(
                                            HttpServletRequest request) {
                                        CorsConfiguration configuration = new CorsConfiguration();

                                        configuration.setAllowedOrigins(
                                                Arrays.asList(
                                                        FRONTEND_DEV_URL,
                                                        CLIENT_USER_URL,
                                                        CLIENT_SHOP_URL,
                                                        CLIENT_USER_DEV_URL,
                                                        CLIENT_SHOP_DEV_URL));
                                        configuration.setAllowCredentials(true);

                                        configuration.setAllowedHeaders(
                                                Collections.singletonList("*"));

                                        configuration.setAllowedHeaders(
                                                Arrays.asList(
                                                        "authorization_user",
                                                        "authorization_shop",
                                                        "Content-Type"));
                                        configuration.setExposedHeaders(
                                                Arrays.asList(
                                                        "Set-Cookie",
                                                        "authorization_user",
                                                        "authorization_shop",
                                                        "Content-Type"));

                                        configuration.setMaxAge(3600L);
                                        return configuration;
                                    }
                                }));

        // csrf disable
        http.csrf((auth) -> auth.disable());

        // From 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        // JWTFilter 추가
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // oauth2
        http.oauth2Login(
                (oauth2) ->
                        oauth2.userInfoEndpoint(
                                        (userInfoEndpointConfig) ->
                                                userInfoEndpointConfig.userService(
                                                        customOAuth2UserService))
                                .successHandler(customSuccessHandler));

        // 경로별 인가 작업
        http.authorizeHttpRequests(
                (auth) ->
                        auth
                                //                        todo : 개발 진행을 위해 임의로 모두 허용
                                //
                                // .requestMatchers("/api/**").authenticated()
                                //
                                // .requestMatchers("/api/oauth2/**").permitAll()
                                .anyRequest()
                                .permitAll());

        // 세션 설정 : STATELESS
        http.sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
