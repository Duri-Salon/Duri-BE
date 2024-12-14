package kr.com.duri.groomer.application.mapper;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.application.dto.PortfolioDetailResponse;
import kr.com.duri.groomer.application.dto.response.FeedbackDetailResponse;
import kr.com.duri.groomer.application.dto.response.PortfolioListResponse;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.FeedbackImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedbackMapper {

    private final CommonMapper commonMapper;

    public FeedbackDetailResponse toFeedbackDetailResponse(Feedback feedback, List<String> images) {
        return FeedbackDetailResponse.builder()
                .feedbackId(feedback.getId())
                .friendly(feedback.getFriendly().getDescription())
                .reaction(feedback.getReaction().getDescription())
                .matter(feedback.getMatter().getDescription())
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

    public PortfolioDetailResponse toPortfolioDetailResponse(Feedback feedback, List<String> images) {
        return PortfolioDetailResponse.builder()
                .feedbackId(feedback.getId())
                .friendly(feedback.getFriendly().getDescription())
                .reaction(feedback.getReaction().getDescription())
                .matter(feedback.getMatter().getDescription())
                .behavior(feedback.getBehavior().getDescription())
                .portfolioContent(feedback.getPortfolioContent())
                .feedbackImages(images)
                .build();
    }
}
