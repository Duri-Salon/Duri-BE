package kr.com.duri.groomer.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.ShopTagService;
import kr.com.duri.groomer.domain.entity.ShopTag;
import kr.com.duri.groomer.repository.ShopTagRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopTagServiceImpl implements ShopTagService {

    private final ShopTagRepository shopTagRepository;

    @Override
    public List<String> findTagsByShopId(Long shopId) {
        return shopTagRepository.findByShopId(shopId).stream()
                .map(ShopTag::getTagName)
                .collect(Collectors.toList());
    }
}
