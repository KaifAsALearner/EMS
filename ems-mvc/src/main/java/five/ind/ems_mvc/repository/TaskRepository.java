package five.ind.ems_mvc.repository;

import five.ind.ems_mvc.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject_ProjectId(Long projectId);
    List<Task> findByTeam_TeamId(Long teamId);
    List<Task> findByAssignedTo_EmpId(Long empId);
}
