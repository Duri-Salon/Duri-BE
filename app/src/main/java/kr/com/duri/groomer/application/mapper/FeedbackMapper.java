package kr.com.duri.groomer.application.mapper;

import java.util.List;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.application.dto.response.*;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.FeedbackImage;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.domain.entity.Pet;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackMapper {

    private final CommonMapper commonMapper;

    public FeedbackDetailResponse toFeedbackDetailResponse(Feedback feedback, List<String> images) {
        return FeedbackDetailResponse.builder()
                .feedbackId(feedback.getId())
                .friendly(feedback.getFriendly().getDescription())
                .reaction(feedback.getReaction().getDescription())
                .behavior(feedback.getBehavior().getDescription())
                .noticeContent(feedback.getNoticeContent())
                .portfolioContent(feedback.getPortfolioContent())
                .expose(feedback.getExpose())
                .deleted(feedback.getDeleted())
                .feedbackImages(images)
                .build();
    }

    public PortfolioListResponse toPortfolioListResponse(Feedback feedback, FeedbackImage image) {
        return PortfolioListResponse.builder()
                .feedbackId(feedback.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }

    public PortfolioDetailResponse toPortfolioDetailResponse(
            Feedback feedback, List<String> images, Groomer groomer, Pet pet) {
        return PortfolioDetailResponse.builder()
                .feedbackId(feedback.getId())
                .friendly(feedback.getFriendly().getDescription())
                .reaction(feedback.getReaction().getDescription())
                .behavior(feedback.getBehavior().getDescription())
                .portfolioContent(feedback.getPortfolioContent())
                .feedbackDate(feedback.getCreatedAt())
                .feedbackImages(images)
                .petInfo(toPetInfoResponse(pet))
                .groomerInfo(toGroomerInfo(groomer))
                .build();
    }

    public DiaryDetailResponse toDiaryDetailResponse(Feedback feedback, List<String> images) {
        return DiaryDetailResponse.builder()
                .feedbackId(feedback.getId())
                .friendly(feedback.getFriendly().getDescription())
                .reaction(feedback.getReaction().getDescription())
                .behavior(feedback.getBehavior().getDescription())
                .noticeContent(feedback.getNoticeContent())
                .feedbackImages(images)
                .build();
    }

    public FeedbackDataResponse toFeedbackDataResponse(
            String friendly, String reaction, String behavior) {
        return FeedbackDataResponse.builder()
                .friendly(friendly)
                .reaction(reaction)
                .behavior(behavior)
                .build();
    }

    public PetInfoResponse toPetInfoResponse(Pet pet) {
        return PetInfoResponse.builder()
                .name(pet.getName())
                .breed(pet.getBreed())
                .gender(pet.getGender().toString())
                .age(pet.getAge())
                .weight(pet.getWeight())
                .neutralized(pet.getNeutering())
                .imageUrl(pet.getImage())
                .build();
    }

    private GroomerInfoResponse toGroomerInfo(Groomer groomer) {
        return GroomerInfoResponse.builder()
                .id(groomer.getId())
                .name(groomer.getName())
                .profileImageUrl(groomer.getImage())
                .build();
    }
}
