package kr.com.duri.groomer.application.mapper;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.application.dto.response.EntryGroomerResponse;
import kr.com.duri.groomer.application.dto.response.EntryResponse;
import kr.com.duri.groomer.application.dto.response.EntryShopResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntryMapper {

    private final CommonMapper commonMapper;

    public EntryResponse toEntryResponse(Shop shop, Groomer groomer) {
        EntryShopResponse entryShopResponse =
                EntryShopResponse.builder()
                        .shopId(shop.getId())
                        .shopName(shop.getName())
                        .shopAddress(shop.getAddress())
                        .shopPhone(shop.getPhone())
                        .shopLat(shop.getLat())
                        .shopLon(shop.getLon())
                        .businessRegistrationNumber(shop.getBusinessRegistrationNumber())
                        .groomerLicenseNumber(shop.getGroomerLicenseNumber())
                        .build();

        EntryGroomerResponse entryGroomerResponse =
                (groomer != null)
                        ? EntryGroomerResponse.builder()
                                .groomerName(groomer.getName())
                                .groomerGender(groomer.getGender().toString())
                                .groomerAge(groomer.getAge())
                                .license(commonMapper.toListString(groomer.getLicense()))
                                .build()
                        : null;

        return EntryResponse.builder()
                .shop(entryShopResponse)
                .groomer(entryGroomerResponse)
                .build();
    }
}
