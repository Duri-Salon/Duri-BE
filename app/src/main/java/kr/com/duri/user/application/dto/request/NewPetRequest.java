package kr.com.duri.user.application.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPetRequest {
    private String name;
    private String breed;
    private Integer age;
    private Float weight;
    private String gender;
    private Boolean neutering;
    private List<String> character;
    private List<String> diseases;
}
