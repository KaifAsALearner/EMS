package five.ind.ems_mvc.service.impl;

import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Project;
import five.ind.ems_mvc.entity.Task;
import five.ind.ems_mvc.entity.Team;
import five.ind.ems_mvc.entity.enums.TaskStatus;
import five.ind.ems_mvc.repository.EmployeeRepository;
import five.ind.ems_mvc.repository.ProjectRepository;
import five.ind.ems_mvc.repository.TaskRepository;
import five.ind.ems_mvc.repository.TeamRepository;
import five.ind.ems_mvc.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    public TaskServiceImpl(TaskRepository tr, TeamRepository ter, ProjectRepository pr, EmployeeRepository er) {
        this.taskRepository = tr;
        this.teamRepository = ter;
        this.projectRepository = pr;
        this.employeeRepository = er;
    }

    @Override
    public void updateEndDate(Long taskId, java.time.LocalDate endDate) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setEndDate(endDate);
        taskRepository.save(task);
    }

    @Override
    public Task createTask(Long managerId, Long projectId, Long teamId, String name, String description, String priority, Long assignedEmpId, LocalDate start, LocalDate due) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();
        Employee manager = employeeRepository.findById(managerId).orElseThrow();
        Employee assignee = employeeRepository.findById(assignedEmpId).orElseThrow();
        if (!team.getManager().getEmpId().equals(managerId)) {
            throw new RuntimeException("Only team manager can create tasks for their team.");
        }
        if (assignee.getTeam() == null || !assignee.getTeam().getTeamId().equals(teamId)) {
            throw new RuntimeException("Must assign to team member!");
        }
        if (project.getTeam() == null || !project.getTeam().getTeamId().equals(teamId)) {
            throw new RuntimeException("Project must be assigned to this team.");
        }
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStartDate(start);
        task.setDueDate(due);
        task.setEndDate(null);
        task.setAssignedTo(assignee);
        task.setTeam(team);
        task.setProject(project);
        task.setStatus(TaskStatus.ASSIGNED);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksForProject(Long projectId) {
        return taskRepository.findByProject_ProjectId(projectId);
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public void updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        taskRepository.save(task);
    }
}
