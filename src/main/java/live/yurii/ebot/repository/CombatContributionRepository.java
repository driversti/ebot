package live.yurii.ebot.repository;

import live.yurii.ebot.entity.CombatContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombatContributionRepository extends JpaRepository<CombatContribution, Long> {
}