package kr.com.duri.groomer.application.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroomerOnboardingInfo {
    private String name;
    private String gender;
    private Integer age;
    private Integer history; // 미용사 경력 (월수)
    private List<String> license;
}
