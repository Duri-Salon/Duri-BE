package kr.com.duri.groomer.application.mapper;

import java.util.Collections;
import java.util.List;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.application.dto.response.GroomerDetailResponse;
import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroomerMapper {

    private final CommonMapper commonMapper;

    public GroomerDetailResponse toGroomerDetailResponse(Groomer groomer) {
        return GroomerDetailResponse.builder()
                .image(groomer.getImage()) // 미용사 이미지
                .name(groomer.getName()) // 미용사 이름
                .history(groomer.getHistory()) // 미용사 경력
                .info(groomer.getInfo()) // 미용사 자기소개
                .build();
    }

    public GroomerProfileDetailResponse toGroomerProfileDetailResponse(Groomer newGroomer) {
        List<String> licenseToString = commonMapper.toListString(newGroomer.getLicense());
        return GroomerProfileDetailResponse.builder()
                .id(newGroomer.getId()) // 미용사 ID
                .email(newGroomer.getEmail()) // 미용사 이메일
                .phone(newGroomer.getPhone()) // 미용사 전화번호
                .name(newGroomer.getName()) // 미용사 이름
                .gender(newGroomer.getGender().toString()) // 미용사 성별
                .age(newGroomer.getAge()) // 미용사 나이
                .history(newGroomer.getHistory()) // 미용사 경력
                .image(newGroomer.getImage()) // 미용사 이미지
                .info(newGroomer.getInfo()) // 미용사 자기소개
                .license(licenseToString) // 자격증
                .build();
    }

    public List<GroomerProfileDetailResponse> toGroomerProfileDetailResponseList(List<Groomer> groomers) {
        if (groomers == null) {
            return Collections.emptyList();
        }
        return groomers.stream()
                .map(this::toGroomerProfileDetailResponse)
                .toList();
    }
}
