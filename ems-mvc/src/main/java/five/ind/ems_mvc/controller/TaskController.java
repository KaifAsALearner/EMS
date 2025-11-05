package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Project;
import five.ind.ems_mvc.entity.Task;
import five.ind.ems_mvc.entity.Team;
import five.ind.ems_mvc.entity.enums.TaskStatus;
import five.ind.ems_mvc.service.EmployeeService;
import five.ind.ems_mvc.service.ProjectService;
import five.ind.ems_mvc.service.TaskService;
import five.ind.ems_mvc.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final EmployeeService employeeService;
    private final TeamService teamService;
    private final ProjectService projectService;

    public TaskController(TaskService ts, EmployeeService es, TeamService tms, ProjectService ps) {
        this.taskService = ts;
        this.employeeService = es;
        this.teamService = tms;
        this.projectService = ps;
    }

    // View tasks for a given project
    @GetMapping("/project/{projectId}")
    public String viewProjectTasks(@PathVariable Long projectId, Principal principal, Model model) {
        Employee user = employeeService.findByUsername(principal.getName());
        Project project = projectService.getProjectById(projectId);
        Team team = project.getTeam();
        boolean isManager = user.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER");
        boolean inTeam = team != null && user.getTeam() != null && user.getTeam().getTeamId().equals(team.getTeamId());
        if (!inTeam && !isManager) {
            model.addAttribute("error", "Access denied.");
            return "tasks";
        }
        List<Task> tasks = taskService.getTasksForProject(projectId);
        List<Employee> members = teamService.getTeamMembers(team.getTeamId());
        boolean isTeamManager = team != null && team.getManager().getEmpId().equals(user.getEmpId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("members", members);
        model.addAttribute("project", project);
        model.addAttribute("team", team);
        model.addAttribute("isManager", isTeamManager);
        model.addAttribute("userId", user.getEmpId());
        return "tasks";
    }

    // Create a new task for a project and team
    @PostMapping("/project/{projectId}/create")
    public String createTask(@PathVariable Long projectId,
                             @RequestParam Long teamId,
                             @RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String priority,
                             @RequestParam Long assignedEmpId,
                             @RequestParam String startDate,
                             @RequestParam String dueDate,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            taskService.createTask(manager.getEmpId(), projectId, teamId, name, description, priority, assignedEmpId,
                    LocalDate.parse(startDate), LocalDate.parse(dueDate));
            redirectAttributes.addFlashAttribute("message", "Task created!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tasks/project/" + projectId;
    }

    // Update task status (employee or team manager)
    @PostMapping("/{taskId}/status")
    public String updateStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus status,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        Employee user = employeeService.findByUsername(principal.getName());
        try {
            Task task = taskService.getTaskById(taskId);
            boolean isAssignee = task.getAssignedTo().getEmpId().equals(user.getEmpId());
            boolean isTeamManager = task.getProject().getTeam().getManager().getEmpId().equals(user.getEmpId());
            TaskStatus current = task.getStatus();
            boolean allowed = false;
            // Transition logic
            if (isAssignee && (current == TaskStatus.ASSIGNED || current == TaskStatus.REOPENED) && status == TaskStatus.IN_PROGRESS) {
                allowed = true;
            } else if (isAssignee && current == TaskStatus.IN_PROGRESS && status == TaskStatus.COMPLETED) {
                allowed = true;
            } else if (isTeamManager && current == TaskStatus.COMPLETED && (status == TaskStatus.VERIFIED || status == TaskStatus.REOPENED)) {
                allowed = true;
            }

            if (allowed) {
                task.setStatus(status);
                if (status == TaskStatus.VERIFIED) {
                    taskService.updateEndDate(taskId, java.time.LocalDate.now());
                } else if (status == TaskStatus.REOPENED) {
                    taskService.updateEndDate(taskId, null); // Clear endDate if reopened
                }
                // Save any other updates (e.g., status change)
                taskService.updateTaskStatus(taskId, status);
                redirectAttributes.addFlashAttribute("message", "Task status updated!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid status transition or not authorized.");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        Task t = taskService.getTaskById(taskId);
        return "redirect:/tasks/project/" + t.getProject().getProjectId();
    }



}