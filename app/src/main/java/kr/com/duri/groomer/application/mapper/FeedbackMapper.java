package kr.com.duri.groomer.application.mapper;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.application.dto.response.FeedbackDetailResponse;
import kr.com.duri.groomer.domain.entity.Feedback;
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
                .feedbackImages(images)
                .build();
    }
}
