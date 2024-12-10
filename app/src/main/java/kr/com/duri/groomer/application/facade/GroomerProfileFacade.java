package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.request.GroomerDetailRequest;
import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
import kr.com.duri.groomer.application.mapper.GroomerMapper;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroomerProfileFacade {

    private final ShopService shopService;

    private final GroomerService groomerService;

    private final GroomerMapper groomerMapper;

    public GroomerProfileDetailResponse createGroomerProfile(
            String token, GroomerDetailRequest groomerDetailRequest) {
        Long shopId = shopService.getShopIdByToken(token);

        Shop shop = shopService.findById(shopId);

        Groomer newGroomer = groomerService.createNewGroomer(shop, groomerDetailRequest);

        return groomerMapper.toGroomerProfileDetailResponse(newGroomer);
    }

    public GroomerProfileDetailResponse getGroomerProfile(Long groomerId) {
        Groomer groomer = groomerService.findById(groomerId);

        return groomerMapper.toGroomerProfileDetailResponse(groomer);
    }

    public GroomerProfileDetailResponse updateGroomerProfile(
            Long groomerId, GroomerDetailRequest groomerDetailRequest) {
        Groomer groomer = groomerService.findById(groomerId);

        groomer = groomerService.updateGroomer(groomer, groomerDetailRequest);

        return groomerMapper.toGroomerProfileDetailResponse(groomer);
    }

    public void deleteGroomerProfile(Long groomerId) {
        Groomer groomer = groomerService.findById(groomerId);
        groomerService.deleteGroomer(groomer);
    }
}
