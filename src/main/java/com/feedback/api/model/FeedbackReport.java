package com.feedback.api.model;

import com.feedback.api.enums.Score;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback_report")
public class FeedbackReport {

  @Id
  @Column(name = "store_id")
  private String storeId;

  @Column(name = "rank")
  @Enumerated(EnumType.STRING)
  private Score rank;

  @Column(name = "last_rank")
  private Score lastRank;

  public FeedbackReport() {}

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public Score getRank() {
    return rank;
  }

  public void setRank(Score rank) {
    this.rank = rank;
  }

  public Score getLastRank() {
    return lastRank;
  }

  public void setLastRank(Score lastRank) {
    this.lastRank = lastRank;
  }
}
