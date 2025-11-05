package five.ind.ems_mvc.service;

import five.ind.ems_mvc.entity.Project;

import java.util.List;

public interface ProjectService {
//    List<Project> getProjectsByDepartment(Long deptId);
    void saveProject(Project project);
    Project createProject(Long managerId, String name, String description, String priority);
    void assignTeamToProject(Long managerId, Long projectId, Long teamId);
    List<Project> getAllProjects();
    List<Project> getProjectsByTeamId(Long teamId);
    Project getProjectById(Long projectId);
}
