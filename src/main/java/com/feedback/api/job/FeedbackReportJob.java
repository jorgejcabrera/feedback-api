package com.feedback.api.job;

import com.feedback.api.enums.FeedbackStatus;
import com.feedback.api.model.Feedback;
import com.feedback.api.repository.FeedbackRepository;
import com.feedback.api.service.FeedbackReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackReportJob {

  @Autowired FeedbackRepository feedbackRepository;
  @Autowired FeedbackReportService feedbackReportService;
  private static final Logger log = LoggerFactory.getLogger(FeedbackReportJob.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  @Scheduled(fixedDelay = 8000)
  public void feedbackReport() {
    log.debug("Creating feedback report: {}", dateFormat.format(new Date()));

    int page = 0;
    int psize = 50;
    Pageable pageable = PageRequest.of(page, psize);
    List<String> storeIds =
        feedbackRepository.findAllStoreId(pageable).stream().collect(Collectors.toList());

    while (!storeIds.isEmpty()) {
      storeIds
          .parallelStream()
          .forEach(
              storeId -> {
                List<Feedback> feedbacks =
                    feedbackRepository.findAllByStoreIdAndStatus(
                        storeId, FeedbackStatus.PENDING_REPORT);
                feedbackReportService.createReport(feedbacks, storeId);
                feedbacks
                    .parallelStream()
                    .forEach(
                        feedback -> {
                          feedback.setStatus(FeedbackStatus.COMPLETED);
                          feedbackRepository.save(feedback);
                        });
              });
      page++;
      psize += psize;
      pageable = PageRequest.of(page, psize);
      storeIds = feedbackRepository.findAllStoreId(pageable).stream().collect(Collectors.toList());
    }
  }
}
