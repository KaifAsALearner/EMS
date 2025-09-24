package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Access;
import five.ind.ems_mvc.entity.compositeId.AccessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends JpaRepository<Access, AccessId> {
}
