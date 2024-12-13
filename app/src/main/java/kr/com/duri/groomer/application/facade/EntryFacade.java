package kr.com.duri.groomer.application.facade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.response.EntryResponse;
import kr.com.duri.groomer.application.mapper.EntryMapper;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntryFacade {

    private final ShopService shopService;
    private final GroomerService groomerService;
    private final EntryMapper entryMapper;

    public List<EntryResponse> getWaitingEntries() {
        return shopService.getEntryWaitingShops().stream()
                .map(
                        shop -> {
                            Optional<Groomer> groomerOpt =
                                    groomerService.findGroomerByShopId(shop.getId());
                            return entryMapper.toEntryResponse(shop, groomerOpt.orElse(null));
                        })
                .collect(Collectors.toList());
    }

    public List<EntryResponse> getApprovedEntries() {
        return shopService.getEntryApprovedShops().stream()
                .map(
                        shop -> {
                            Optional<Groomer> groomerOpt =
                                    groomerService.findGroomerByShopId(shop.getId());
                            return entryMapper.toEntryResponse(shop, groomerOpt.orElse(null));
                        })
                .collect(Collectors.toList());
    }
}
