package kr.com.duri.groomer.application.facade;

import java.util.List;

import kr.com.duri.groomer.application.dto.request.GroomerDetailRequest;
import kr.com.duri.groomer.application.dto.response.GroomerAndShopProfileRespnse;
import kr.com.duri.groomer.application.dto.response.GroomerMyPageResponse;
import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
import kr.com.duri.groomer.application.mapper.GroomerMapper;
import kr.com.duri.groomer.application.mapper.ShopMapper;
import kr.com.duri.groomer.application.service.*;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.service.RequestService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class GroomerProfileFacade {

    private final ShopService shopService;

    private final ShopImageService shopImageService;

    private final ShopTagService shopTagService;

    private final GroomerService groomerService;

    private final RequestService requestService;

    private final QuotationService quotationService;

    private final GroomerMapper groomerMapper;

    private final ShopMapper shopMapper;

    public GroomerProfileDetailResponse createGroomerProfile(
            String token, GroomerDetailRequest groomerDetailRequest, MultipartFile img) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        String imageUrl = groomerService.uploadGroomerImage(img);
        Groomer newGroomer = groomerService.createNewGroomer(shop, groomerDetailRequest, imageUrl);
        return groomerMapper.toGroomerProfileDetailResponse(newGroomer);
    }

    public GroomerAndShopProfileRespnse getGroomerProfile(Long groomerId) {
        Groomer groomer = groomerService.findById(groomerId);
        Shop shop = groomer.getShop();
        ShopImage shopImage = shopImageService.getMainShopImage(shop);
        String imageUrl = shopImage == null ? null : shopImage.getShopImageUrl();
        List<String> shopTags = shopTagService.findTagsByShopId(shop.getId());
        return GroomerAndShopProfileRespnse.builder()
                .groomerProfileDetail(groomerMapper.toGroomerProfileDetailResponse(groomer))
                .shopProfileDetail(shopMapper.toShopProfileDetailResponse(shop, imageUrl, shopTags))
                .build();
    }

    public GroomerProfileDetailResponse updateGroomerProfile(
            Long groomerId, GroomerDetailRequest groomerDetailRequest, MultipartFile img) {
        Groomer groomer = groomerService.findById(groomerId);
        String imageUrl = groomerService.uploadGroomerImage(img);
        groomer = groomerService.updateGroomer(groomer, groomerDetailRequest, imageUrl);
        return groomerMapper.toGroomerProfileDetailResponse(groomer);
    }

    public void deleteGroomerProfile(Long groomerId) {
        Groomer groomer = groomerService.findById(groomerId);
        groomerService.deleteGroomer(groomer);
    }

    public GroomerMyPageResponse getMyPageInfo(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        ShopImage shopImage = shopImageService.getMainShopImage(shop);
        String imageUrl = shopImage == null ? null : shopImage.getShopImageUrl();
        List<String> shopTags = shopTagService.findTagsByShopId(shop.getId());
        Groomer groomer = groomerService.getGroomerByShopId(shop.getId());
        Integer reservationCount = requestService.getApprovedHistoryByShopId(shopId).size();
        Integer noShowCount = quotationService.getNoShowHistoryByShopId(shopId).size();
        return GroomerMyPageResponse.builder()
                .shopProfileDetailResponse(
                        shopMapper.toShopProfileDetailResponse(shop, imageUrl, shopTags))
                .groomerProfileDetailResponse(groomerMapper.toGroomerProfileDetailResponse(groomer))
                .reservationCount(reservationCount)
                .noShowCount(noShowCount)
                .build();
    }
}
