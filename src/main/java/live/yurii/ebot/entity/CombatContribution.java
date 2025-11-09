package live.yurii.ebot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import live.yurii.ebot.model.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "combat_contributions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CombatContribution {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "citizen_id", nullable = false)
  private Citizen citizen;

  @Enumerated(EnumType.STRING)
  @Column(name = "for_country", nullable = false)
  private Country forCountry;

  @Column(name = "damage", nullable = false)
  private Long damage = 0L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "round_id", nullable = false)
  private Round round;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Override
  public int hashCode() {
    int result = Objects.hashCode(id);
    result = 31 * result + Objects.hashCode(citizen);
    result = 31 * result + Objects.hashCode(forCountry);
    result = 31 * result + Objects.hashCode(round);
    return result;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof CombatContribution that)) return false;

    return Objects.equals(id, that.id)
      && Objects.equals(citizen, that.citizen)
      && forCountry == that.forCountry
      && Objects.equals(round, that.round);
  }
}
