package com.feedback.api.service;

import com.feedback.api.builder.FeedbackBuilder;
import com.feedback.api.dto.FeedbackDTO;
import com.feedback.api.exception.BadRequestException;
import com.feedback.api.exception.EntityNotFoundException;
import com.feedback.api.model.Feedback;
import com.feedback.api.repository.FeedbackRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceTest {

  private static Long nextOrderId = 1L;
  private static final Date from =
      Date.from(
          LocalDate.now().minusDays(31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  private static final Date to =
      Date.from(
          LocalDate.now().plusDays(31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  @Mock FeedbackRepository feedbackRepository;
  @InjectMocks FeedbackService feedbackService;
  @Rule public ExpectedException exception = ExpectedException.none();

  @Test
  public void when_createFeedbackForOrderWithPreviousFeedback_then_shouldReturnBadRequest() {
    exception.expect(BadRequestException.class);
    exception.expectMessage(
        String.format("Feedback for order %s is already registered.", ++nextOrderId));
    // given
    Long orderId = nextOrderId;
    Feedback feedback = new FeedbackBuilder().withOrderId(orderId).build();

    // when
    when(feedbackRepository.findById(nextOrderId)).thenReturn(Optional.of(feedback));

    // then
    feedbackService.create(feedback);
  }

  @Test
  public void when_tryToRetrieveAnEntityThatNotExists_then_shouldReturnNotFoundException() {
    exception.expect(EntityNotFoundException.class);
    exception.expectMessage(
        String.format("%s with id %s not found.", Feedback.class.getSimpleName(), ++nextOrderId));

    // when
    when(feedbackRepository.findById(nextOrderId)).thenReturn(Optional.empty());

    // then
    feedbackService.retrieve(nextOrderId);
  }

  @Test
  public void when_tryToUpdateAnEntityThatNotExists_then_shouldReturnNotFoundException() {
    exception.expect(EntityNotFoundException.class);
    exception.expectMessage(
        String.format("%s with id %s not found.", Feedback.class.getSimpleName(), ++nextOrderId));
    // given
    Long orderId = nextOrderId;
    FeedbackDTO feedback = new FeedbackDTO();

    // when
    when(feedbackRepository.findById(nextOrderId)).thenReturn(Optional.empty());

    // then
    feedbackService.update(orderId, feedback);
  }

  @Test
  public void when_tryToUpdateAnEntity_then_shouldSetOnlyNewCommentAndTrimIt() {
    // given
    Long orderId = nextOrderId;
    Feedback feedback =
        new FeedbackBuilder()
            .withComment("It was a previous comment.")
            .withOrderId(orderId)
            .build();

    FeedbackDTO body = new FeedbackDTO("\t It is a new comment\n");
    // when
    when(feedbackRepository.findById(nextOrderId)).thenReturn(Optional.of(feedback));

    // then
    assertThat(feedbackService.update(orderId, body).getComment()).isEqualTo("It is a new comment");
  }

  @Test
  public void when_tryToGetFeedbackByUserIdWithoutAnyFeedback_thenShouldReturnNotFoundException() {
    // given
    exception.expect(EntityNotFoundException.class);
    exception.expectMessage(String.format("%s with id %s not found.", "User", ++nextOrderId));
    Long userId = nextOrderId;
    Pageable pageable = PageRequest.of(0, 2);

    // when
    when(feedbackRepository.existsBuyerId(Mockito.anyLong())).thenReturn(false);

    // then
    feedbackService.getAllFeedbacksByBuyerIdBetween(userId, from, to, pageable);
  }

  @Test
  public void when_thereIsNotFeedbackForUser_then_shouldWorkOk() {
    // given
    Long userId = ++nextOrderId;
    Pageable pageable = PageRequest.of(0, 2);

    // when
    when(feedbackRepository.findByBuyerIdAndCreatedDateBetween(
            Mockito.anyLong(),
            Mockito.any(Date.class),
            Mockito.any(Date.class),
            Mockito.any(Pageable.class)))
        .thenReturn(Page.empty());
    when(feedbackRepository.existsBuyerId(Mockito.anyLong())).thenReturn(true);

    // then
    assertThat(feedbackService.getAllFeedbacksByBuyerIdBetween(userId, from, to, pageable))
        .isEmpty();
  }

  @Test
  public void when_tryToGetFeedbackByStoreIdWithoutAnyFeedback_thenShouldReturnNotFoundException() {
    // given
    String storeId = "AR1";
    exception.expect(EntityNotFoundException.class);
    exception.expectMessage(String.format("%s with id %s not found.", "Store", storeId));
    Pageable pageable = PageRequest.of(0, 2);

    // when
    when(feedbackRepository.existsStoreId(storeId)).thenReturn(false);

    // then
    feedbackService.getAllFeedbacksByStoreIdBetween(storeId, from, to, pageable);
  }
}
