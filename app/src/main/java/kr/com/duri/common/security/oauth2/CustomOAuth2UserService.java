package kr.com.duri.common.security.oauth2;

import kr.com.duri.common.security.dto.ShopUserDto;
import kr.com.duri.common.security.dto.SiteUserDto;
import kr.com.duri.common.security.provider.NaverOAuth2User;
import kr.com.duri.common.security.provider.NaverShopResponse;
import kr.com.duri.common.security.provider.NaverUserResponse;
import kr.com.duri.common.security.provider.OAuth2Response;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.SiteUser;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SiteUserService siteUserService;
    private final ShopService shopService;

    public CustomOAuth2UserService(SiteUserService siteUserService, ShopService shopService) {
        this.siteUserService = siteUserService;
        this.shopService = shopService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User Info -> " + oAuth2User);
        System.out.println("userRequest : " + userRequest.getClientRegistration());

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인 진행 중인 서비스를 구분하는 코드

        if (registrationId.equals("naver-user")) { // 유저

            NaverUserResponse oAuth2Response = new NaverUserResponse(oAuth2User.getAttributes());
            SiteUserDto siteUserDto = SiteUserDto.builder()
                    .email(oAuth2Response.getEmail())
                    .providerId(oAuth2Response.getProviderId())
                    .provider(userRequest.getClientRegistration().getRegistrationId())
                    .name(oAuth2Response.getName())
                    .birthday(oAuth2Response.getBirthday())
                    .birthyear(oAuth2Response.getBirthyear())
                    .gender(oAuth2Response.getGender())
                    .mobile(oAuth2Response.getMobile())
                    .mobileE164(oAuth2Response.getMobileE164())
                    .role("ROLE_USER")
                    .client("naver_user")
                    .build();


            SiteUser siteUser = siteUserService.findBySocialId(siteUserDto.getProviderId())
                    .orElseGet(() -> siteUserService.saveNewSiteUser(
                            siteUserDto.getProviderId(),
                            siteUserDto.getEmail(),
                            siteUserDto.getName(),
                            siteUserDto.getMobile(),
                            siteUserDto.getGender(),
                            siteUserDto.getBirthday(),
                            siteUserDto.getBirthyear()
                    ));

            siteUserDto.updateId(siteUser.getId());
            siteUserDto.updateNewUser(siteUser.getNewUser());

            return new NaverOAuth2User(siteUserDto);

        } else if (registrationId.equals("naver-shop")) { // 매장

            OAuth2Response oAuth2Response = new NaverShopResponse(oAuth2User.getAttributes());

            ShopUserDto shopUserDto = ShopUserDto.builder()
                    .email(oAuth2Response.getEmail())
                    .providerId(oAuth2Response.getProviderId())
                    .provider(userRequest.getClientRegistration().getRegistrationId())
                    .role("ROLE_SHOP")
                    .client("naver_shop")
                    .build();

            Shop shop = shopService.findBySocialId(shopUserDto.getProviderId())
                    .orElseGet(() -> shopService.saveNewShop(shopUserDto.getProviderId(), shopUserDto.getEmail()));

            shopUserDto.updateId(shop.getId());
            shopUserDto.updateNewUser(shop.getNewShop());

            return new NaverOAuth2User(shopUserDto);

        } else {
            return null;
        }
    }
}
