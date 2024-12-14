package kr.com.duri.groomer.application.service.impl;

import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.application.service.FeedbackService;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.repository.FeedbackReopsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackReopsitory feedbackReopsitory;

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

}
