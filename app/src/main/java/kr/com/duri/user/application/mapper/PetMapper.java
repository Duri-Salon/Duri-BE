package kr.com.duri.user.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.user.application.dto.response.PetDetailResponse;
import kr.com.duri.user.application.dto.response.PetProfileListResponse;
import kr.com.duri.user.application.dto.response.PetProfileResponse;
import kr.com.duri.user.domain.entity.Pet;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
@RequiredArgsConstructor
public class PetMapper {

    private final ObjectMapper objectMapper;

    public List<String> parseJsonArray(String jsonString) {
        try {
            List<String> list =
                    objectMapper.readValue(
                            jsonString,
                            TypeFactory.defaultInstance()
                                    .constructCollectionType(List.class, String.class));
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 문자열을 리스트로 변환할 수 없습니다.", e);
        }
    }

    private String convertListToJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list); // 리스트를 JSON 문자열로 변환
        } catch (JsonProcessingException e) {
            throw new RuntimeException("리스트를 JSON 문자열로 변환하는 중 오류가 발생했습니다.", e);
        }
    }

    public PetDetailResponse toPetResponse(Pet pet) {
        return new PetDetailResponse()
                .builder()
                .name(pet.getName())
                .breed(pet.getBreed())
                .age(pet.getAge())
                .weight(pet.getWeight())
                .gender(pet.getGender())
                .neutering(pet.getNeutering())
                .character(parseJsonArray(pet.getCharacter()))
                .diseases(parseJsonArray(pet.getDiseases()))
                .image(pet.getImage())
                .lastGrooming(pet.getLastGrooming())
                .build();
    }

    public String toStringJson(List<String> list) {
        return convertListToJson(list);
    }

    public PetProfileListResponse toPetProfileListResponse(List<Pet> petList) {
        return new PetProfileListResponse()
                .builder()
                .petProfileList(
                        petList.stream().map(this::toPetProfileResponse).collect(Collectors.toList()))
                .build();
    }

    public PetProfileResponse toPetProfileResponse(Pet pet) {
        return PetProfileResponse.builder()
                .id(pet.getId())
                .image(pet.getImage())
                .name(pet.getName())
                .age(pet.getAge())
                .gender(pet.getGender())
                .breed(pet.getBreed())
                .weight(pet.getWeight())
                .neutering(pet.getNeutering())
                .character(parseJsonArray(pet.getCharacter()))
                .diseases(parseJsonArray(pet.getDiseases()))
                .lastGrooming(pet.getLastGrooming())
                .build();
    }
}
