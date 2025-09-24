package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance,Long> {

}
