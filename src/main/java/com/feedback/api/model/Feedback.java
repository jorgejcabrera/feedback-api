package com.feedback.api.model;

import com.feedback.api.enums.FeedbackStatus;
import com.feedback.api.enums.Score;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback")
public class Feedback extends Auditable {

  @Id
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "seller_id")
  private Long sellerId;
  @Column(name = "buyer_id")
  private Long buyerId;
  @Column(name = "item_id")
  private Long itemId;
  @Column(name = "comment")
  private String comment;
  @Column(name = "store_id")
  private String storeId;
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private FeedbackStatus status;
  @Column(name = "score", nullable = false)
  @Enumerated(EnumType.STRING)
  private Score score;

  public FeedbackStatus getStatus() {
    return status;
  }

  public void setStatus(FeedbackStatus status) {
    this.status = status;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }

  public Long getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(Long buyerId) {
    this.buyerId = buyerId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Feedback() {}
}
