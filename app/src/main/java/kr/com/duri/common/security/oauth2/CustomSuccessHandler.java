package kr.com.duri.common.security.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.com.duri.common.security.jwt.JwtUtil;
import kr.com.duri.common.security.provider.NaverOAuth2User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public CustomSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //    todo : 도메인 단위 분리 고려 및 리팩토링 진행
    //    todo : 프론트엔드 리턴값 확인
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        NaverOAuth2User customUserDetails = (NaverOAuth2User) authentication.getPrincipal();

        Long id = customUserDetails.getId();
        String username = customUserDetails.getProvider();
        String providerId = customUserDetails.getName();
        Boolean newUser = customUserDetails.getNewUser();
        System.out.println("id : " + id);
        System.out.println("username : " + username);
        System.out.println("providerId : " + providerId);
        System.out.println("newUser : " + newUser);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        System.out.println("role : " + role);

        String token = jwtUtil.createJwt(id, providerId, role, 60 * 60 * 60 * 60L);

        String tokenName =
                username.equals("naver-user")
                        ? "authorization_user"
                        : username.equals("naver-shop") ? "authorization_shop" : "error";

        if (tokenName.equals("error")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse =
                    String.format(
                            "{\n"
                                    + "\t\t\"success\": false, \n"
                                    + "\t\t\"response\": null, \n"
                                    + "\t\t\"error\": {\n"
                                    + "        \"message\": \"유효한 유저가 아닙니다.\",\n"
                                    + "        \"status\": 401\n"
                                    + "    }\n"
                                    + "}");

            response.getWriter().write(jsonResponse);
            response.getWriter().flush();

        } else {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse =
                    String.format(
                            "{\n"
                                    + "  \"success\": true,\n"
                                    + "  \"response\": {\n"
                                    + "    \"client\": \"%s\",\n"
                                    + "    \"token\": \"%s\",\n"
                                    + "    \"newUser\": %s\n"
                                    + "  },\n"
                                    + "  \"error\": null\n"
                                    + "}",
                            tokenName, token, newUser);

            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        }
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
