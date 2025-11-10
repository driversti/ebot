package live.yurii.ebot.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<City> cities = new ArrayList<>();

}
