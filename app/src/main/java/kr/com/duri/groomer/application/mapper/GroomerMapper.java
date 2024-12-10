package kr.com.duri.groomer.application.mapper;

import kr.com.duri.groomer.application.dto.response.GroomerDetailResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroomerMapper {

    public GroomerDetailResponse toGroomerDetailResponse(Groomer groomer) {
        return GroomerDetailResponse.builder()
                .image(groomer.getImage()) // 미용사 이미지
                .name(groomer.getName()) // 미용사 이름
                .history(groomer.getHistory()) // 미용사 경력
                .info(groomer.getInfo()) // 미용사 자기소개
                .build();
    }
}
