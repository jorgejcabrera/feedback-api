package com.feedback.api.service;

import com.feedback.api.builder.FeedbackBuilder;
import com.feedback.api.dto.FeedbackDTO;
import com.feedback.api.enums.FeedbackStatus;
import com.feedback.api.enums.Score;
import com.feedback.api.exception.BadRequestException;
import com.feedback.api.exception.EntityNotFoundException;
import com.feedback.api.model.Feedback;
import com.feedback.api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

  private static String USER = "User";
  private static String STORE = "Store";
  @Autowired FeedbackRepository feedbackRepository;
  @Autowired FeedbackReportService feedbackReportService;
  @Autowired FeedbackMessageSender feedbackMessageSender;

  public Feedback create(Feedback body) {
    Optional<Feedback> opFeedback = feedbackRepository.findById(body.getOrderId());
    if (opFeedback.isPresent() && opFeedback.get().getStatus() != FeedbackStatus.DELETE)
      throw new BadRequestException(
          String.format("Feedback for order %s is already registered.", body.getOrderId()));

    Feedback newFeedback =
        new FeedbackBuilder()
            .withItemId(body.getItemId())
            .withOrderId(body.getOrderId())
            .withBuyerId(body.getBuyerId())
            .withSellerId(body.getSellerId())
            .withStoreId(body.getStoreId())
            .withScore(body.getScore())
            .withComment(body.getComment())
            .build();
    feedbackRepository.save(newFeedback);
    feedbackMessageSender.sendFeedback(newFeedback);
    return newFeedback;
  }

  public Feedback retrieve(Long id) {
    Feedback feedback =
        feedbackRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(Feedback.class.getSimpleName(), id.toString()));
    if (feedback.getStatus() == FeedbackStatus.DELETE)
      throw new EntityNotFoundException(Feedback.class.getSimpleName(), id.toString());

    return feedback;
  }

  @Transactional
  public Feedback update(Long id, FeedbackDTO body) {
    Feedback feedback = retrieve(id);
    String comment = body.getComment() != null ? body.getComment().trim() : null;
    feedback.setComment(comment);
    if (body.getScore() != null) {
      updateStatus(feedback, body.getScore());
    }
    feedbackRepository.save(feedback);
    feedbackMessageSender.sendFeedback(feedback);
    return feedback;
  }

  @Transactional
  public void delete(Long id) {
    Feedback feedback = retrieve(id);
    if (feedback.getStatus() == FeedbackStatus.COMPLETED) {
      feedbackReportService.removeFeedbackFromReport(feedback);
    }
    feedback.setStatus(FeedbackStatus.DELETE);
    feedbackMessageSender.sendFeedback(feedback);
  }

  public List<Feedback> getAllFeedbacksByBuyerIdBetween(
      Long userId, Date from, Date to, Pageable pageable) {
    if (!feedbackRepository.existsBuyerId(userId))
      throw new EntityNotFoundException(USER, userId.toString());
    return feedbackRepository.findByBuyerIdAndCreatedDateBetween(userId, from, to, pageable)
        .stream()
        .filter(feedback -> feedback.getStatus() != FeedbackStatus.DELETE)
        .collect(Collectors.toList());
  }

  public List<Feedback> getAllFeedbacksByStoreIdBetween(
      String storeId, Date from, Date to, Pageable pageable) {
    if (!feedbackRepository.existsStoreId(storeId))
      throw new EntityNotFoundException(STORE, storeId);
    return feedbackRepository.findByStoreIdAndCreatedDateBetween(storeId, from, to, pageable)
        .stream()
        .filter(feedback -> feedback.getStatus() != FeedbackStatus.DELETE)
        .collect(Collectors.toList());
  }

  protected void updateStatus(Feedback feedback, Score score) {
    if (feedback.isReported()) {
      feedbackReportService.removeFeedbackFromReport(feedback);
    }
    feedback.setScore(score);
    feedback.setStatus(FeedbackStatus.PENDING_REPORT);
  }
}
