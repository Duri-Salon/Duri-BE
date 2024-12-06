package kr.com.duri.common.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.com.duri.common.security.dto.NaverUserDto;
import kr.com.duri.common.security.provider.NaverOAuth2User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// todo : 통합 테스트 진행하며 필요한 부분 리팩토링
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${client.user.urls}")
    private String userUrls;

    @Value("${client.shop.urls}")
    private String shopUrls;

    public JwtFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String origin = request.getHeader("Origin");
        System.out.println("origin: " + origin);
        String host = request.getHeader("Host");
        System.out.println("Host: " + host);

        // todo : 환경 변수에서 못읽어 오는 부분이 있어 리팩토링 중에 수정하겠습니다.
        //        List<String> userUrlList = parseUrlsFromEnv(userUrls);
        //        List<String> shopUrlList = parseUrlsFromEnv(shopUrls);
        List<String> userUrlList =
                Arrays.asList("http://localhost:3000", "https://duri-saloncom.vercel.app");
        List<String> shopUrlList =
                Arrays.asList("http://localhost:3001", "https://salon-duri-salon.vercel.app");

        System.out.println("userUrls: " + Arrays.toString(userUrlList.toArray()));
        System.out.println("shopUrls: " + Arrays.toString(shopUrlList.toArray()));

        String client = null;
        String token = null;

        if (origin != null) {
            if (userUrlList.stream().anyMatch(origin::contains)) {
                client = "naver_user";
                token = request.getHeader("authorization_user");
            } else if (shopUrlList.stream().anyMatch(origin::contains)) {
                client = "naver_shop";
                token = request.getHeader("authorization_shop");
            }
        }

//        client = "naver_user";
//        token = request.getHeader("authorization_user");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        System.out.println("client: " + client);
        System.out.println("token: " + token);

        try {
            if (token == null || client == null) {
                System.out.println("Token is missing or client type is undefined");
                filterChain.doFilter(request, response);
                return;
            }

            String providerId = jwtUtil.getProviderId(token);

            NaverUserDto naverUserDto =
                    NaverUserDto.builder().providerId(providerId).client(client).build();

            NaverOAuth2User customOAuth2User = new NaverOAuth2User(naverUserDto);

            Authentication authToken =
                    new UsernamePasswordAuthenticationToken(
                            customOAuth2User, null, customOAuth2User.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT token");
        } catch (Exception e) {
            throw new RuntimeException("Unknown error");
        }

        //        if (jwtUtil.isExpired(token)) {
        //            String refreshToken = request.getHeader("refresh_token");
        //            if (refreshToken == null || !refreshTokenService.validate(refreshToken)) {
        //                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //                return;
        //            }
        //            String newToken = refreshTokenService.generateNewToken(refreshToken);
        //            response.setHeader("Authorization", "Bearer " + newToken);
        //        }

        filterChain.doFilter(request, response);
    }

    private List<String> parseUrlsFromEnv(String envVariable) {
        if (envVariable == null || envVariable.isBlank()) {
            return Collections.emptyList();
        }
        return List.of(envVariable.split(","));
    }
}
