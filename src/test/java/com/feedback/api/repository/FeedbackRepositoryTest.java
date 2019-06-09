package com.feedback.api.repository;

import com.feedback.api.enums.FeedbackStatus;
import com.feedback.api.enums.Score;
import com.feedback.api.model.Feedback;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FeedbackRepositoryTest {

  private static Long nextOrderId = 1L;
  private static final String storeId = "AR1";
  private static final Date from =
      Date.from(
          LocalDate.now().minusDays(31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  private static final Date to =
      Date.from(
          LocalDate.now().plusDays(31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  @Autowired private FeedbackRepository feedbackRepository;

  @AfterEach
  void clean() {
    feedbackRepository.deleteAll();
  }

  @Test
  public void when_createFeedbackEntity_thenShouldWorkOk() {
    Feedback feedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    feedbackRepository.save(feedback);
    assertThat(feedbackRepository.existsById(feedback.getOrderId())).isTrue();
  }

  @Test
  public void when_createFeedbacksEntity_thenShouldReturnAllList() {
    feedbackRepository.save(buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE));
    feedbackRepository.save(buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE));
    feedbackRepository.save(buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE));
    feedbackRepository.save(buildFeedback(Score.BRONCE, FeedbackStatus.DELETE));
    feedbackRepository.save(buildFeedback(Score.BRONCE, FeedbackStatus.DELETE));

    assertThat(feedbackRepository.findAllByStoreIdAndStatus("AR1", FeedbackStatus.ACTIVE))
        .hasSize(3);
    assertThat(feedbackRepository.findAllByStoreIdAndStatus("AR1", FeedbackStatus.DELETE))
        .hasSize(2);
  }

  @Test
  public void when_findFeedbacksBetweenDates_thenShouldWorkOk() {
    // given
    Long buyerId = 1L;
    Pageable pageable = PageRequest.of(0, 2);
    Feedback oneFeedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    oneFeedback.setBuyerId(buyerId);
    Feedback twoFeedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    twoFeedback.setBuyerId(2L);
    Feedback threeFeedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    threeFeedback.setBuyerId(buyerId);
    List<Feedback> allFeedbacks = Arrays.asList(oneFeedback, twoFeedback, threeFeedback);

    // when
    feedbackRepository.saveAll(allFeedbacks);

    // then
    assertThat(
            feedbackRepository.findByBuyerIdAndCreatedDateBetween(buyerId, from, to, pageable)
                .stream()
                .collect(Collectors.toList()))
        .hasSize(2);
  }

  @Test
  public void when_findFeedbacksBetweenDatesWithPageParam_thenShouldReturnAValidPage() {
    // given
    Long buyerId = 1L;
    Pageable pageable = PageRequest.of(1, 2);
    Feedback oneFeedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    oneFeedback.setBuyerId(buyerId);
    Feedback twoFeedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    twoFeedback.setBuyerId(2L);
    Feedback threeFeedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    threeFeedback.setBuyerId(buyerId);
    Feedback fourFeedback = buildFeedback(Score.BRONCE, FeedbackStatus.ACTIVE);
    fourFeedback.setBuyerId(buyerId);
    List<Feedback> allFeedbacks =
        Arrays.asList(oneFeedback, twoFeedback, threeFeedback, fourFeedback);

    // when
    feedbackRepository.saveAll(allFeedbacks);

    // then
    assertThat(
            feedbackRepository.findByBuyerIdAndCreatedDateBetween(buyerId, from, to, pageable)
                .stream()
                .collect(Collectors.toList()))
        .hasSize(1);
  }

  public Feedback buildFeedback(Score score, FeedbackStatus status) {
    Feedback feedback = new Feedback();
    feedback.setStoreId(storeId);
    feedback.setOrderId(nextOrderId++);
    feedback.setScore(Score.BRONCE);
    feedback.setCreatedDate(new Date());
    feedback.setLastModifiedDate(new Date());
    feedback.setScore(score);
    feedback.setStoreId(storeId);
    feedback.setStatus(status);
    return feedback;
  }
}
