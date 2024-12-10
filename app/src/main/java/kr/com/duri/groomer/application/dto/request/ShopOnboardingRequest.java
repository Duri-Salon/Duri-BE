package kr.com.duri.groomer.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopOnboardingRequest {
    private ShopOnboardingInfo shopOnboardingInfo;
    private GroomerOnboardingInfo groomerOnboardingInfo;
}
