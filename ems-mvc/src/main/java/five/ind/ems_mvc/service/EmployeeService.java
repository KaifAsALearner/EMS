package five.ind.ems_mvc.service;

import five.ind.ems_mvc.dto.EmployeeDto;
import five.ind.ems_mvc.entity.Employee;

import java.util.List;

public interface EmployeeService {
    void saveEmployee(EmployeeDto employeeDto);
    Employee findByUsername(String username);
    Employee findById(Long empId);
    List<Employee> findAllEmployees();
    List<String> findEmails(long employeeId);
    List<String> findPhones(long employeeId);
    Employee updateEmployee(Employee employee, boolean updatePassword);  // NEW
    void addEmail(long employeeId, String email);  // NEW
    void removeEmail(long employeeId, String email);  // NEW
    void addPhone(long employeeId, String phoneNo);  // NEW
    void removePhone(long employeeId, String phoneNo);  // NEW

    List<Employee> getEmployeesByDepartment(Long deptId);
    void changeEmployeeRole(Long managerId, Long employeeId, String newRoleName);
}
