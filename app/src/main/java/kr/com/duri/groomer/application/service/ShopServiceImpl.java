package kr.com.duri.groomer.application.service;

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
}
