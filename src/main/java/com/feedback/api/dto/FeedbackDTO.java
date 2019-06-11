package com.feedback.api.dto;

import com.feedback.api.enums.Score;

public class FeedbackDTO {

  private String comment;
  private Long sellerId;
  private Long itemId;
  private Score score;

  public FeedbackDTO() {}

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }

  public FeedbackDTO(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }
}
