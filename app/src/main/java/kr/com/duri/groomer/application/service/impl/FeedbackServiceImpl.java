package kr.com.duri.groomer.application.service.impl;

import kr.com.duri.common.exception.NotFoundException;
import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.application.service.FeedbackService;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.repository.FeedbackReopsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackReopsitory feedbackReopsitory;

    @Override
    public Feedback getFeedbackById(Long feedbackId) {
        return feedbackReopsitory.findById(feedbackId).orElseThrow(() -> new NotFoundException("피드백이 존재하지 않습니다."));
    }

    @Override
    public Feedback saveNewFeedback(Quotation quotation, Groomer groomer, NewFeedbackRequest newFeedbackRequest) {
        return feedbackReopsitory.save(Feedback.createNewFeedback(
                quotation, groomer,
                newFeedbackRequest.getFriendly(),
                newFeedbackRequest.getReaction(),
                newFeedbackRequest.getMatter(),
                newFeedbackRequest.getBehavior(),
                newFeedbackRequest.getNoticeContent(),
                newFeedbackRequest.getPortfolioContent(),
                newFeedbackRequest.getExpose()));
    }

    @Override
    public List<Feedback> getPortfolioList(Long groomerId) {
        return feedbackReopsitory.findByGroomerIdAndExposeTrueAndDeletedFalseOrderByCreatedAtDesc(groomerId);
    }

}
