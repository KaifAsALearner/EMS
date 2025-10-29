package five.ind.ems_mvc.service;

import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Team;

import java.util.List;

public interface TeamService {
    List<Team> getTeamsByDepartment(Long deptId);
    Team createTeam(Long managerId, String teamName);
    void addEmployeeToTeam(Long managerId, Long teamId, Long employeeId);
    void removeEmployeeFromTeam(Long managerId, Long teamId, Long employeeId);
    void assignTeamManager(Long managerId, Long teamId, Long employeeId);
    void deleteTeam(Long managerId, Long teamId);
    List<Employee> getAvailableEmployees(Long managerId);
    List<Employee> getTeamMembers(Long teamId);
}
