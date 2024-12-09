package kr.com.duri.groomer.application.service.impl;

import java.util.List;
import java.util.Optional;

import kr.com.duri.common.security.jwt.JwtUtil;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.groomer.repository.ShopRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    private final JwtUtil jwtUtil;

    @Override
    public boolean existsByShopId(Long shopId) {
        return shopRepository.existsById(shopId);
    }

    @Override
    public Optional<Shop> findBySocialId(String socialId) {
        return shopRepository.findBySocialId(socialId);
    }

    @Override
    public Shop saveNewShop(String socialId, String email) {
        Shop newShop = Shop.createNewShop(socialId, email);
        return shopRepository.save(newShop);
    }

    @Override
    public Shop findById(Long shopId) {
        return shopRepository
                .findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("해당 매장을 찾을 수 없습니다."));
    }

    @Override
    public String createNewShopJwt(Shop shop) {
        return jwtUtil.createJwt(shop.getId(), shop.getSocialId(), 60 * 60 * 60 * 60L);
    }

    @Override
    public List<Object[]> findShopsWithinRadius(Double lat, Double lon, Double radius) {
        return shopRepository.findShopsWithinRadius(lat, lon, radius);
    }

    @Override
    public List<Object[]> findShopsWithSearch(String search, Double lat, Double lon) {
        return shopRepository.findShopsWithSearch(search, lat, lon);
    }

    // 매장 평점 업데이트
    @Override
    public Shop updateShopRating(Long shopId, Float rating) {
        Shop shop = findById(shopId);
        shop.updateRating(Float.valueOf(rating));
        return shopRepository.save(shop);
    }

    // 추천 매장 조회
    @Override
    public List<Shop> findShopsByRadius(Double lat, Double lon, Double radians) {
        return shopRepository.findShopsByRadius(lat, lon, radians);
    }

    // 토큰으로 매장 아이디 찾기
    @Override
    public Long getShopIdByToken(String token) {
        token = jwtUtil.removeBearer(token);
        return jwtUtil.getId(token);
    }
}
