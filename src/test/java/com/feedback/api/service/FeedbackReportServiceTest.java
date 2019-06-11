package com.feedback.api.service;

import com.feedback.api.builder.FeedbackBuilder;
import com.feedback.api.enums.Score;
import com.feedback.api.model.Feedback;
import com.feedback.api.model.FeedbackReport;
import com.feedback.api.repository.FeedbackReportRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

public class FeedbackReportServiceTest {

  @Mock FeedbackReportRepository feedbackReportRepository;
  @InjectMocks FeedbackReportService feedbackReportService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void when_createNewReportFromFeedbacks_then_shouldWorkOk() {
    // given
    String storeId = "AR1";
    Feedback oneFeedback =
        new FeedbackBuilder().withScore(Score.DIAMOND).withStoreId(storeId).withOrderId(1L).build();
    Feedback twoFeedback =
        new FeedbackBuilder().withScore(Score.DIAMOND).withStoreId(storeId).withOrderId(2L).build();
    List<Feedback> feedbacks = Arrays.asList(oneFeedback, twoFeedback);
    FeedbackReport report = new FeedbackReport();
    report.setRank(Score.DIAMOND);

    // when
    when(feedbackReportRepository.findById(storeId)).thenReturn(Optional.empty());
    when(feedbackReportRepository.save(Mockito.any(FeedbackReport.class))).thenReturn(report);

    // then
    FeedbackReport feedbackReport = feedbackReportService.createReport(feedbacks, storeId);
    assertThat(feedbackReport).isNotNull();
    assertThat(feedbackReport.getRank()).isEqualTo(Score.DIAMOND);
  }

  @Test
  public void when_removeFeedbackFromReport_then_shouldWorkOk() {
    // given
    String storeId = "AR1";
    Feedback oneFeedback =
        new FeedbackBuilder().withScore(Score.BRONCE).withStoreId(storeId).withOrderId(1L).build();

    FeedbackReport report = new FeedbackReport();
    report.addPoints((long) Score.BRONCE.getValue());
    report.addPoints((long) Score.DIAMOND.getValue());
    report.increaseTotalCount(2);
    report.buildRank();

    assertThat(report.getRank()).isEqualTo(Score.GOLD);

    // when
    when(feedbackReportRepository.findById(storeId)).thenReturn(Optional.of(report));
    when(feedbackReportRepository.save(Mockito.any(FeedbackReport.class))).thenReturn(report);

    // then
    feedbackReportService.removeFeedbackFromReport(oneFeedback);
    assertThat(report.getRank()).isEqualTo(Score.DIAMOND);
  }
}
