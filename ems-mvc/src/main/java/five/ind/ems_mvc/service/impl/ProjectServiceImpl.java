package five.ind.ems_mvc.service.impl;

import five.ind.ems_mvc.entity.Project;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Team;
import five.ind.ems_mvc.entity.enums.ProjectStatus;
import five.ind.ems_mvc.repository.ProjectRepository;
import five.ind.ems_mvc.repository.EmployeeRepository;
import five.ind.ems_mvc.repository.TeamRepository;
import five.ind.ems_mvc.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public void saveProject(Project project) {
        projectRepository.save(project);
    }


    @Override
    public List<Project> getAllProjects() {
        List<Project> list = projectRepository.findAll();
        return list == null ? List.of() : list;
    }


    @Override
    public List<Project> getProjectsByTeamId(Long teamId){
        return projectRepository.findByTeam_TeamId(teamId);
    }

//    @Override
//    public List<Project> getProjectsByDepartment(Long deptId){
//        return projectRepository.findByDepartment_DeptId(deptId);
//    }

    @Override
    public Project createProject(Long managerId, String name, String description, String priority) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        if (!manager.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER")) {
            throw new RuntimeException("Only managers can create projects!");
        }
        if (projectRepository.existsByName(name)) {
            throw new RuntimeException("Project name already exists!");
        }
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setPriority(priority);
        project.setStatus(ProjectStatus.ACTIVE);
//        project.setDepartment(manager.getRole().getDepartment());
        return projectRepository.save(project);
    }

    @Override
    public void assignTeamToProject(Long managerId, Long projectId, Long teamId){
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Long deptId = manager.getRole().getDepartment().getDeptId();
//        if (!project.getDepartment().getDeptId().equals(deptId) || !team.getManager().getRole().getDepartment().getDeptId().equals(deptId)){
//            throw new RuntimeException("Can only assign your department's team/project!");
//        }
        project.setTeam(team);
        projectRepository.save(project);
    }

    @Override
    public Project getProjectById(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
