package kr.com.duri.groomer.application.service;

import java.util.Optional;

import kr.com.duri.groomer.domain.entity.Shop;
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
        return shopRepository.save(Shop.builder().socialId(socialId).email(email).build());
    }
}
