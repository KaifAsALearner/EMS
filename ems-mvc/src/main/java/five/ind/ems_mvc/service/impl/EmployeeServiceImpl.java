package five.ind.ems_mvc.service.impl;

import five.ind.ems_mvc.dto.EmployeeDto;
import five.ind.ems_mvc.entity.Department;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Role;
import five.ind.ems_mvc.entity.compositeId.RoleId;
import five.ind.ems_mvc.repository.DepartmentRepository;
import five.ind.ems_mvc.repository.EmployeeRepository;
import five.ind.ems_mvc.repository.RoleRepository;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               RoleRepository roleRepository,
                               DepartmentRepository departmentRepository,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFName(employeeDto.getFName());
        employee.setLName(employeeDto.getLName());
        employee.setUsername(employeeDto.getUsername());
        employee.setGender(employeeDto.getGender());
        employee.setDob(employeeDto.getDob());
        employee.setHiredDate(LocalDate.now()); // Set a hired date

        // Encrypt the password using the PasswordEncoder bean
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

        // Find or create the default role for new users
        Role defaultRole = checkOrCreateDefaultRole();
        employee.setRole(defaultRole);

        employeeRepository.save(employee);
    }

    @Override
    public Employee findByUsername(String username) {
        return employeeRepository.findEmployeeByUsername(username).orElse(null);
    }

    @Override
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        // Use a stream to map each Employee entity to an EmployeeDto
        return employees.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    // Private helper method to convert an Employee entity to a DTO
    private EmployeeDto convertEntityToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFName(employee.getFName());
        employeeDto.setLName(employee.getLName());
        employeeDto.setUsername(employee.getUsername());
        employeeDto.setGender(employee.getGender());
        employeeDto.setDob(employee.getDob());
        return employeeDto;
    }

    // Private helper method to find or create the default role for new employees
    private Role checkOrCreateDefaultRole() {
        // First, ensure a default Department exists
        Department generalDept = departmentRepository.findByName("General")
                .orElseGet(() -> {
                    Department newDept = new Department();
                    newDept.setName("General");
                    newDept.setDesc("Default department for new employees");
                    return departmentRepository.save(newDept);
                });

        // Now, find or create the default Role within that Department
        RoleId defaultRoleId = new RoleId(generalDept.getDeptId(), "EMPLOYEE");
        return roleRepository.findById(defaultRoleId)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setId(defaultRoleId);
                    newRole.setDepartment(generalDept);
                    return roleRepository.save(newRole);
                });
    }
}