package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.entity.Project;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Team;
import five.ind.ems_mvc.entity.enums.ProjectStatus;
import five.ind.ems_mvc.service.ProjectService;
import five.ind.ems_mvc.service.TeamService;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TeamService teamService;
    private final EmployeeService employeeService;

    public ProjectController(ProjectService projectService, TeamService teamService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.teamService = teamService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listProjects(Principal principal, Model model) {
        Employee user = employeeService.findByUsername(principal.getName());
        boolean isManager = user.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER");
        List<Project> projects = isManager ? projectService.getAllProjects() :
                (user.getTeam() != null ? projectService.getProjectsByTeamId(user.getTeam().getTeamId()) : List.of());
        model.addAttribute("isManager", isManager);
        model.addAttribute("employee", user);
        model.addAttribute("projects", projects);
        // For manager: provide all teams for assignment dropdown
        if (isManager) {
            List<Team> allTeams = teamService.getAllTeams();
            model.addAttribute("allTeams", allTeams);
        }
        return "projects";
    }

    @PostMapping("/create")
    public String createProject(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam String priority,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            projectService.createProject(manager.getEmpId(), name, description, priority);
            redirectAttributes.addFlashAttribute("message", "Project created successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/projects";
    }

    @PostMapping("/{projectId}/assign")
    public String assignTeamToProject(@PathVariable Long projectId,
                                      @RequestParam Long teamId,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            projectService.assignTeamToProject(manager.getEmpId(), projectId, teamId);
            redirectAttributes.addFlashAttribute("message", "Team assigned to project!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/projects";
    }

    @PostMapping("/updateStatus")
    public String updateProjectStatus(@RequestParam("projectId") Long projectId,
                                      @RequestParam("status") ProjectStatus status,
                                      RedirectAttributes redirectAttributes) {
        try {
            Project project = projectService.getProjectById(projectId);
            project.setStatus(status);
            projectService.saveProject(project); // Make sure you have this method
            redirectAttributes.addFlashAttribute("message", "Project status updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to update project status: " + e.getMessage());
        }
        return "redirect:/projects";
    }

}
