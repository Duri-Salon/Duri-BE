package kr.com.duri.groomer.application.service;

import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.groomer.repository.ShopRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

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

    public List<Object[]> findShopsWithinRadius(Double lat, Double lon, Double radius) {
        return shopRepository.findShopsWithinRadius(lat, lon, radius);
    }
}
