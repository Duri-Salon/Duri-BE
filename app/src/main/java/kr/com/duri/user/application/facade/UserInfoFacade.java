package kr.com.duri.user.application.facade;

import kr.com.duri.common.security.jwt.JwtUtil;
import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.application.dto.response.PetDetailResponse;
import kr.com.duri.user.application.mapper.PetMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.SiteUser;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoFacade {

    private final SiteUserService siteUserService;

    private final PetService petService;

    private final PetMapper petMapper;

    public PetDetailResponse createNewPet(String token, NewPetRequest newPetRequest) {
        Long userId = siteUserService.getUserIdByToken(token);

        SiteUser siteUser = siteUserService.getSiteUserById(userId);

        Pet pet = petService.save(petService.createNewPet(siteUser, newPetRequest));

        return petMapper.toPetResponse(pet);
    }
}
