package com.feedback.api.enums;

public enum Score {
  BRONCE(1),
  SILVER(2),
  GOLD(3),
  PLATINUM(4),
  DIAMOND(5);

  int value;

  Score(int score) {
    this.value = score;
  }

  public int getValue() {
    return value;
  }

  public static Score getScore(int value) {
    Score s = null;
    for (Score score : Score.values()) {
      if (score.getValue() == value) s = score;
    }
    return s;
  }
}
