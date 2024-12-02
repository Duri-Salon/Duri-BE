package kr.com.duri.common.security.provider;

import kr.com.duri.common.security.dto.NaverUserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class NaverOAuth2User implements OAuth2User {

    private final NaverUserDto naverUserDto;

    public NaverOAuth2User(NaverUserDto userDto) {
        this.naverUserDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return naverUserDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() { // 식별자 - 소설 고유 아이디
        return naverUserDto.getProviderId();
    }

    public Long getId() {
        return naverUserDto.getId();
    }

    public String getEmail() {
        return naverUserDto.getEmail();
    }

    public String getProvider() {
        return naverUserDto.getProvider();
    }

    public Boolean getNewUser() {
        return naverUserDto.getNewUser();
    }
}
