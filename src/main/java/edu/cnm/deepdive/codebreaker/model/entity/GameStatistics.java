package edu.cnm.deepdive.codebreaker.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrimaryKeyJoinColumn;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect("SELECT * FROM game_statistics")
public class GameStatistics {

  @Id
  @Column(name = "player_id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "player-id")
  @MapsId
  @JsonProperty(access = Access.READ_ONLY)
  private User player;

  @Id
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private int poolSize;

  @Id
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private int codeLength;

  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private int gamesPlayed;

  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private double avgGuessCount;

  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private double avgDuration;

  public Long getId() {
    return id;
  }

  public User getPlayer() {
    return player;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public int getGamesPlayed() {
    return gamesPlayed;
  }

  public double getAvgGuessCount() {
    return avgGuessCount;
  }

  public double getAvgDuration() {
    return avgDuration;
  }

}
