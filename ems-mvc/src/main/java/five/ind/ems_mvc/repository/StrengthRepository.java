package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Strength;
import five.ind.ems_mvc.entity.compositeId.StrengthId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrengthRepository extends JpaRepository<Strength, StrengthId> {
}
