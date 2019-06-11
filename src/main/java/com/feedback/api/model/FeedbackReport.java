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
public class FeedbackReport extends Auditable {

  @Id
  @Column(name = "store_id")
  private String storeId;

  @Column(name = "points")
  private long points;

  @Column(name = "total_count")
  private long totalCount;

  @Column(name = "rank")
  @Enumerated(EnumType.STRING)
  private Score rank;

  public FeedbackReport() {}

  public FeedbackReport(String storeId) {
    this.storeId = storeId;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public Score getRank() {
    return rank;
  }

  public void buildRank() {
    this.rank = Score.getScore(Math.round(this.points / this.totalCount));
  }

  public void addPoints(Long points) {
    this.points += points;
  }

  public void decreasePoints(Long points) {
    this.points -= points;
  }

  public void increaseTotalCount(int count) {
    this.totalCount += count;
  }

  public void decreaseTotalCount(int count) {
    this.totalCount -= count;
  }

  public Long getPoints() {
    return points;
  }

  public void setPoints(Long points) {
    this.points = points;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public void setRank(Score rank) {
    this.rank = rank;
  }
}
