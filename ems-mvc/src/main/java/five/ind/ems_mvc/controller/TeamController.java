package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.entity.Team;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.service.TeamService;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final EmployeeService employeeService;

    public TeamController(TeamService teamService, EmployeeService employeeService) {
        this.teamService = teamService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listTeams(Principal principal, Model model) {
        Employee currentUser = employeeService.findByUsername(principal.getName());
        boolean isManager = currentUser.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER");

        List<Team> teams;
        List<Employee> availableEmployees = List.of();

        if (isManager) {
            Long deptId = currentUser.getRole().getDepartment().getDeptId();
            teams = teamService.getTeamsByDepartment(deptId);
            availableEmployees = teamService.getAvailableEmployees(currentUser.getEmpId());
        } else {
            // Non-managers see all teams (or filter as needed)
            Long deptId = currentUser.getRole().getDepartment().getDeptId();
            teams = teamService.getTeamsByDepartment(deptId);
        }

        // For each team, get members
        Map<Long, List<Employee>> teamMembersMap = new HashMap<>();
        for (Team team : teams) {
            List<Employee> members = teamService.getTeamMembers(team.getTeamId());
            teamMembersMap.put(team.getTeamId(), members);
        }

        model.addAttribute("teams", teams);
        model.addAttribute("teamMembersMap", teamMembersMap);
        model.addAttribute("availableEmployees", availableEmployees);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isManager", isManager);

        return "teams";
    }


    @PostMapping("/create")
    public String createTeam(@RequestParam String teamName,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            teamService.createTeam(manager.getEmpId(), teamName);
            redirectAttributes.addFlashAttribute("message", "Team created successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teams";
    }

    @PostMapping("/{teamId}/add")
    public String addEmployeeToTeam(@PathVariable Long teamId,
                                    @RequestParam Long employeeId,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            teamService.addEmployeeToTeam(manager.getEmpId(), teamId, employeeId);
            redirectAttributes.addFlashAttribute("message", "Employee added to team!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teams";
    }

    @PostMapping("/{teamId}/remove")
    public String removeEmployeeFromTeam(@PathVariable Long teamId,
                                         @RequestParam Long employeeId,
                                         Principal principal,
                                         RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            teamService.removeEmployeeFromTeam(manager.getEmpId(), teamId, employeeId);
            redirectAttributes.addFlashAttribute("message", "Employee removed from team!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teams";
    }

    @PostMapping("/{teamId}/manager")
    public String assignTeamManager(@PathVariable Long teamId,
                                    @RequestParam Long employeeId,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            teamService.assignTeamManager(manager.getEmpId(), teamId, employeeId);
            redirectAttributes.addFlashAttribute("message", "Team manager assigned!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teams";
    }

    @PostMapping("/{teamId}/delete")
    public String deleteTeam(@PathVariable Long teamId,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        Employee manager = employeeService.findByUsername(principal.getName());
        try {
            teamService.deleteTeam(manager.getEmpId(), teamId);
            redirectAttributes.addFlashAttribute("message", "Team deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teams";
    }
}

