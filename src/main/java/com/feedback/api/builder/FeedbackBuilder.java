package com.feedback.api.builder;

import com.feedback.api.enums.FeedbackStatus;
import com.feedback.api.enums.Score;
import com.feedback.api.model.Feedback;

public class FeedbackBuilder {

  private Long orderId;
  private Long sellerId;
  private Long buyerId;
  private Long itemId;
  private String comment;
  private String storeId;
  private Score score;

  public FeedbackBuilder() {}

  public FeedbackBuilder withOrderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  public FeedbackBuilder withSellerId(Long sellerId) {
    this.sellerId = sellerId;
    return this;
  }

  public FeedbackBuilder withBuyerId(Long buyerId) {
    this.buyerId = buyerId;
    return this;
  }

  public FeedbackBuilder withItemId(Long itemId) {
    this.itemId = itemId;
    return this;
  }

  public FeedbackBuilder withComment(String comment) {
    this.comment = comment != null ? comment.trim() : null;
    return this;
  }

  public FeedbackBuilder withStoreId(String storeId) {
    this.storeId = storeId != null ? storeId.toUpperCase().trim() : null;
    return this;
  }

  public FeedbackBuilder withScore(Score score) {
    this.score = score;
    return this;
  }

  public Feedback build() {
    Feedback feedback = new Feedback();
    feedback.setStatus(FeedbackStatus.PENDING_REPORT);
    feedback.setOrderId(this.orderId);
    feedback.setComment(this.comment);
    feedback.setSellerId(this.sellerId);
    feedback.setBuyerId(this.buyerId);
    feedback.setItemId(this.itemId);
    feedback.setStoreId(this.storeId);
    feedback.setScore(this.score);
    return feedback;
  }
}
