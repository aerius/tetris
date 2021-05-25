package nl.aerius.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TetrisScore {
  private String name;
  private int score;
  private Date date;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void setScore(final int score) {
    this.score = score;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }
}
