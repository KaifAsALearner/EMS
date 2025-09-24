package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Weakness;
import five.ind.ems_mvc.entity.compositeId.WeaknessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeaknessRepository extends JpaRepository<Weakness, WeaknessId> {
}
