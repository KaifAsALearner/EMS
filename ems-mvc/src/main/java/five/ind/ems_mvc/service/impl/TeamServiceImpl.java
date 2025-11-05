package five.ind.ems_mvc.service.impl;

import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Team;
import five.ind.ems_mvc.repository.EmployeeRepository;
import five.ind.ems_mvc.repository.TeamRepository;
import five.ind.ems_mvc.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    public TeamServiceImpl(TeamRepository teamRepository, EmployeeRepository employeeRepository) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public List<Team> getTeamsByDepartment(Long deptId) {
        return teamRepository.findByManager_Role_Department_DeptId(deptId);
    }

    @Override
    public Team createTeam(Long managerId, String teamName) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if (!manager.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER")) {
            throw new RuntimeException("Only managers can create teams!");
        }

        // Check if team name already exists
        if (teamRepository.existsByName(teamName)) {
            throw new RuntimeException("Team name already exists! Please choose a different name.");
        }

        Team team = new Team();
        team.setName(teamName);
        team.setManager(manager);
        return teamRepository.save(team);
    }

    @Override
    public void addEmployeeToTeam(Long managerId, Long teamId, Long employeeId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Check manager permissions
        if (!manager.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER")) {
            throw new RuntimeException("Only managers can add employees to teams!");
        }

        // Check same department
        Long managerDeptId = manager.getRole().getDepartment().getDeptId();
        if (!employee.getRole().getDepartment().getDeptId().equals(managerDeptId)) {
            throw new RuntimeException("Employee not in your department!");
        }

        // Check employee not already in a team
        if (employee.getTeam() != null) {
            throw new RuntimeException("Employee already belongs to a team!");
        }

        // Add employee to team
        employee.setTeam(team);
        employeeRepository.save(employee);
    }

    @Override
    public void removeEmployeeFromTeam(Long managerId, Long teamId, Long employeeId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!manager.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER")) {
            throw new RuntimeException("Only managers can remove employees from teams!");
        }

        employee.setTeam(null);
        employeeRepository.save(employee);
    }

    @Override
    public void assignTeamManager(Long managerId, Long teamId, Long employeeId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        Employee newTeamManager = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!manager.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER")) {
            throw new RuntimeException("Only managers can assign team managers!");
        }

        // Check new team manager is in the team
        if (!newTeamManager.getTeam().getTeamId().equals(teamId)) {
            throw new RuntimeException("Employee must be in the team to become team manager!");
        }

        team.setManager(newTeamManager);
        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long managerId, Long teamId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (!manager.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER")) {
            throw new RuntimeException("Only managers can delete teams!");
        }

        // Remove all employees from team first
        List<Employee> teamMembers = employeeRepository.findByTeam_TeamId(teamId);
        for (Employee emp : teamMembers) {
            emp.setTeam(null);
            employeeRepository.save(emp);
        }

        teamRepository.delete(team);
    }

    @Override
    public List<Employee> getAvailableEmployees(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Long deptId = manager.getRole().getDepartment().getDeptId();

        return employeeRepository.findByRole_Department_DeptId(deptId)
                .stream()
                .filter(emp -> emp.getTeam() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> getTeamMembers(Long teamId) {
        return employeeRepository.findByTeam_TeamId(teamId);
    }


}
