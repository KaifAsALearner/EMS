package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {
    // Find teams where the manager belongs to a specific department
    List<Team> findByManager_Role_Department_DeptId(Long deptId);

    // Find team by manager ID
    Optional<Team> findByManager_EmpId(Long managerId);

    Optional<Team> findByName(String name);
    boolean existsByName(String name);


}
