package five.ind.ems_mvc.service;

import five.ind.ems_mvc.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getProjectsByDepartment(Long deptId);
    Project createProject(Long managerId, String name, String description);
    void assignTeamToProject(Long managerId, Long projectId, Long teamId);
}
