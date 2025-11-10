package live.yurii.ebot.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import live.yurii.ebot.model.Country;
import live.yurii.ebot.model.WarType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  @EqualsAndHashCode.Include
  private Integer id;

  @Column(name = "war_id", nullable = false)
  private Integer warId;

  @Column(name = "started_at")
  private Instant startedAt;

  @Column(name = "finished_at")
  private Instant finishedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "invader_country", nullable = false)
  private Country invader;

  @Enumerated(EnumType.STRING)
  @Column(name = "defender_country", nullable = false)
  private Country defender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_id", nullable = false)
  private Region region;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id", nullable = false)
  private City city;

  @Enumerated(EnumType.STRING)
  @Column(name = "war_type", nullable = false)
  private WarType warType;

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("id ASC")
  private List<Round> rounds = new ArrayList<>();

  public Optional<Round> findRoundById(Integer roundId) {
    return rounds.stream()
      .filter(round -> round.getId().equals(roundId))
      .findFirst();
  }

  public void addRound(Round round) {
    rounds.add(round);
    round.setCampaign(this);
  }

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
