package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {
}
