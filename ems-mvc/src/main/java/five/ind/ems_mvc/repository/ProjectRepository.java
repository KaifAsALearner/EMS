package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
//    List<Project> findByDepartment_DeptId(Long deptId);
    boolean existsByName(String name);
    Optional<Project> findByName(String name);
    List<Project> findByTeam_TeamId(Long teamId);
    List<Project> findAll();
}
