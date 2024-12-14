package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;

import java.util.List;

public interface FeedbackService {
    Feedback saveNewFeedback(Quotation quotation, Groomer groomer, NewFeedbackRequest newFeedbackRequest);

    List<Feedback> getPortfolioList(Long groomerId);

    Feedback getFeedbackById(Long feedbackId);
}
