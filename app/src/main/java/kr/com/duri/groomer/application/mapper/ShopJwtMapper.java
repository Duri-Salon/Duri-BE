package kr.com.duri.groomer.application.mapper;

import kr.com.duri.groomer.application.dto.response.NewShopJwtResponse;
import org.springframework.stereotype.Component;

@Component
public class ShopJwtMapper {

    public NewShopJwtResponse toNewJwtResponse(String client, String token, Boolean newUser) {
        return NewShopJwtResponse.builder()
                .token(token)
                .client(client)
                .newUser(newUser)
                .build();
    }

}
