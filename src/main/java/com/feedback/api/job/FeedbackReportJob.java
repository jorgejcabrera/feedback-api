package com.feedback.api.job;

import com.feedback.api.enums.FeedbackStatus;
import com.feedback.api.enums.Score;
import com.feedback.api.model.Feedback;
import com.feedback.api.model.FeedbackReport;
import com.feedback.api.repository.FeedbackReportRepository;
import com.feedback.api.repository.FeedbackRepository;
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
  @Autowired FeedbackReportRepository feedbackReportRepository;
  private static final Logger log = LoggerFactory.getLogger(FeedbackReportJob.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  @Scheduled(fixedDelay = 4000)
  public void feedbackReport() {
    log.info("Creating feedback report: {}", dateFormat.format(new Date()));

    int page = 0;
    int psize = 1;
    Pageable pageable = PageRequest.of(page, psize);
    List<String> storeIds =
        feedbackRepository.findAllStoreId(pageable).stream().collect(Collectors.toList());

    while (!storeIds.isEmpty()) {
      storeIds
          .parallelStream()
          .forEach(
              storeId -> {
                List<Feedback> feedbacks =
                    feedbackRepository.findAllByStoreIdAndStatus(storeId, FeedbackStatus.ACTIVE);
                int sum =
                    feedbacks
                        .parallelStream()
                        .map(feedback -> feedback.getScore().getValue())
                        .reduce(0, Integer::sum);
                int size = feedbacks.size();

                FeedbackReport feedbackReport = new FeedbackReport();
                feedbackReport.setStoreId(storeId);
                feedbackReport.setRank(Score.getScore(Math.round(sum / size)));
                feedbackReport.setLastRank(Score.getScore(Math.round(sum / size)));
                feedbackReportRepository.save(feedbackReport);
              });
      page++;
      psize += psize;
      pageable = PageRequest.of(page, psize);
      storeIds = feedbackRepository.findAllStoreId(pageable).stream().collect(Collectors.toList());
    }
  }
}
