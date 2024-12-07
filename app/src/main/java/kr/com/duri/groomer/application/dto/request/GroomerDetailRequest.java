package kr.com.duri.groomer.application.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroomerDetailRequest {
    private String email; // 미용사 이메일
    private String phone; // 미용사 전화번호
    private String name; // 미용사 이름
    private String gender; // 미용사 성별
    private Integer age; // 미용사 나이
    private Integer history; // 미용사 경력 (월수)
    private String image; // 프로필 이미지
    private String info; // 미용사 자기소개
    private List<String> license; // 자격증
}
