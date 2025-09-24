package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Role;
import five.ind.ems_mvc.entity.compositeId.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleId> {
}
