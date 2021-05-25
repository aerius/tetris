package nl.aerius.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "leaderboard")
public class TetrisScore {
  @Id
  @GeneratedValue
  private long id;

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

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }
}
