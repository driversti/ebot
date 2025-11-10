package live.yurii.ebot.repository;

import live.yurii.ebot.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {

  @Query("SELECT c.id FROM Campaign c WHERE c.id IN :campaignIds")
  List<Integer> findExistingIds(@Param("campaignIds") List<Integer> campaignIds);

}
