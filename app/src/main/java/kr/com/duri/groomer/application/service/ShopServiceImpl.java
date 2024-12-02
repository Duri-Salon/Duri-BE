package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    @Override
    public Optional<Shop> findBySocialId(String socialId) {
        return shopRepository.findBySocialId(socialId);
    }

    @Override
    public Shop saveNewShop(String socialId, String email) {
        return shopRepository.save(Shop.builder()
                .socialId(socialId)
                .email(email)
                .build());
    }

}
