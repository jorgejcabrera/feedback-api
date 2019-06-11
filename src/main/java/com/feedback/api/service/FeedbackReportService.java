package com.feedback.api.service;

import com.feedback.api.model.Feedback;
import com.feedback.api.model.FeedbackReport;
import com.feedback.api.repository.FeedbackReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackReportService {

  @Autowired FeedbackReportRepository feedbackReportRepository;

  public FeedbackReport createReport(List<Feedback> feedbacks, String storeId) {
    FeedbackReport report =
        feedbackReportRepository.findById(storeId).orElse(new FeedbackReport(storeId));
    long sum =
        feedbacks
            .parallelStream()
            .map(feedback -> (long) feedback.getScore().getValue())
            .reduce(0L, Long::sum);
    int size = feedbacks.size();
    report.addPoints(sum);
    report.increaseTotalCount(size);
    report.buildRank();
    return feedbackReportRepository.save(report);
  }

  public void removeFeedbackFromReport(Feedback feedback) {
    Optional<FeedbackReport> opReport = feedbackReportRepository.findById(feedback.getStoreId());
    if (opReport.isPresent()) {
      FeedbackReport report = opReport.get();
      report.decreasePoints((long) feedback.getScore().getValue());
      report.decreaseTotalCount(1);
      report.buildRank();
      feedbackReportRepository.save(report);
    }
  }
}
