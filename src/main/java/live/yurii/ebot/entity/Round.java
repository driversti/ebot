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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rounds", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"campaign_id", "round", "division"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Round {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "round", nullable = false)
  private Short round;

  @Enumerated(EnumType.STRING)
  @Column(name = "division", nullable = false, length = 10)
  private Division division;

  @Column(name = "started_at", nullable = false)
  private Instant startedAt;

  @Column(name = "finished_at")
  private Instant finishedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private Campaign campaign;

  @Column(name = "invader_score", nullable = false)
  private Integer invaderScore = 0;

  @Column(name = "defender_score", nullable = false)
  private Integer defenderScore = 0;

  @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<CombatContribution> contributions = new HashSet<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public void addContribution(CombatContribution contribution) {
    if (contributions == null) {
      contributions = new HashSet<>();
    }
    contributions.add(contribution);
    contribution.setRound(this);
  }
}
