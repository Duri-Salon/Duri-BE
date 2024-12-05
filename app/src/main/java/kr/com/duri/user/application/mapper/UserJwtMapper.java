package kr.com.duri.user.application.mapper;

import kr.com.duri.user.application.dto.response.NewUserJwtResponse;
import org.springframework.stereotype.Component;

@Component
public class UserJwtMapper {

    public NewUserJwtResponse toNewUserJwtResponse(String username, String client, String token, Boolean newUser) {
        return NewUserJwtResponse.builder()
                .username(username)
                .token(token)
                .client(client)
                .newUser(newUser)
                .build();
    }

}
