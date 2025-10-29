package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.entity.Project;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Team;
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
        Employee currentUser = employeeService.findByUsername(principal.getName());
        boolean isManager = currentUser.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER");
        Long deptId = currentUser.getRole().getDepartment().getDeptId();

        List<Project> projects = projectService.getProjectsByDepartment(deptId);
        List<Team> teams = teamService.getTeamsByDepartment(deptId);

        model.addAttribute("projects", projects);
        model.addAttribute("teams", teams);
        model.addAttribute("isManager", isManager);

        return "projects";
    }

    @PostMapping("/create")
    public String createProject(@RequestParam String name,
                                @RequestParam String description,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            projectService.createProject(manager.getEmpId(), name, description);
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
}
