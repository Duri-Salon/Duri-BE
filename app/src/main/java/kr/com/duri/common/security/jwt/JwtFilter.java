package kr.com.duri.common.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.com.duri.common.security.dto.NaverUserDto;
import kr.com.duri.common.security.provider.NaverOAuth2User;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// todo : 통합 테스트 진행하며 필요한 부분 리팩토링
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientHost = request.getHeader("Host");
        System.out.println("clientHost: " + clientHost);

        String userHost = System.getenv("CLIENT_USER_URL"); // 환경 변수에서 가져옴
        String shopHost = System.getenv("CLIENT_SHOP_URL");

        String client = null;
        String token = null;

        // 2. 도메인/호스트 기반으로 클라이언트 추출
        if (clientHost != null) {
            if (userHost != null && clientHost.contains(userHost)) {
                client = "naver_user";
                token = request.getHeader("authorization_user");
            } else if (shopHost != null && clientHost.contains(shopHost)) {
                client = "naver_shop";
                token = request.getHeader("authorization_shop");
            }
        }

        // 3. 토큰 및 클라이언트 검증
        if (token == null || client == null) {
            System.out.println("Token is missing or client type is undefined");
            filterChain.doFilter(request, response);
            return;
        }

        // 4. JWT 만료 확인
        if (jwtUtil.isExpired(token)) {
            System.out.println("Token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 5. JWT에서 사용자 정보 추출
        String providerId = jwtUtil.getProviderId(token);

        NaverUserDto naverUserDto =
                NaverUserDto.builder().providerId(providerId).client(client).build();

        NaverOAuth2User customOAuth2User = new NaverOAuth2User(naverUserDto);

        Authentication authToken =
                new UsernamePasswordAuthenticationToken(
                        customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
