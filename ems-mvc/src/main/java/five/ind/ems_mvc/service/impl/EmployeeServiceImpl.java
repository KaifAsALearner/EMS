package five.ind.ems_mvc.service.impl;

import five.ind.ems_mvc.dto.EmployeeDto;
import five.ind.ems_mvc.entity.*;
import five.ind.ems_mvc.entity.compositeId.EmailId;
import five.ind.ems_mvc.entity.compositeId.PhoneId;
import five.ind.ems_mvc.entity.compositeId.RoleId;
import five.ind.ems_mvc.repository.*;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               RoleRepository roleRepository,
                               DepartmentRepository departmentRepository,
                               PasswordEncoder passwordEncoder,
                               EmailRepository emailRepository,
                               PhoneRepository phoneRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailRepository = emailRepository;
        this.phoneRepository = phoneRepository;
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
        return employeeRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Employee findById(Long empId) {
        return employeeRepository.findById(empId).orElse(null);
    }

    @Override
    public List<Employee> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        // Use a stream to map each Employee entity to an EmployeeDto
        return employees;
    }

    @Override
    public List<String> findEmails(long employeeId) {
        List<Email> emails = emailRepository.findByEmployeeEmpId(employeeId);

        return emails.stream()
                .map(e -> e.getId().getEmail())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findPhones(long employeeId) {
        List<Phone> phones = phoneRepository.findByEmployeeEmpId(employeeId);

        return phones.stream()
                .map(e -> e.getId().getPhoneNo())
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
        employeeDto.setEmails(findEmails(employee.getEmpId()));
        employeeDto.setPhones(findPhones(employee.getEmpId()));
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

    @Override
    public Employee updateEmployee(Employee updated, boolean updatePassword) {
        Employee current = employeeRepository.findById(updated.getEmpId()).orElseThrow();

        // Check username uniqueness if allowing username updates
        Optional<Employee> exists = employeeRepository.findByUsername(updated.getUsername());
        if (exists.isPresent() && !exists.get().getEmpId().equals(updated.getEmpId())) {
            throw new RuntimeException("Username is already taken!");
        }

        current.setFName(updated.getFName());
        current.setLName(updated.getLName());
        current.setGender(updated.getGender());
        // ... other editable fields ...

        // Password hashing logic
        if (updatePassword && updated.getPassword() != null && !updated.getPassword().isBlank()) {
            if (!updated.getPassword().startsWith("$2a$")) {
                current.setPassword(passwordEncoder.encode(updated.getPassword()));
            } else {
                current.setPassword(updated.getPassword());
            }
        }
        return employeeRepository.save(current);
    }
    @Override
    public void addEmail(long employeeId, String email) {
        EmailId eid = new EmailId(employeeId, email);
        Employee emp = employeeRepository.findById(employeeId).orElseThrow();
        Email e = new Email();
        e.setId(eid);
        e.setEmployee(emp);
        emailRepository.save(e);
    }
    @Override
    public void removeEmail(long employeeId, String email) {
        EmailId eid = new EmailId(employeeId, email);
        emailRepository.deleteById(eid);
    }
    @Override
    public void addPhone(long employeeId, String phoneNo) {
        PhoneId pid = new PhoneId(employeeId, phoneNo);
        Employee emp = employeeRepository.findById(employeeId).orElseThrow();
        Phone p = new Phone();
        p.setId(pid);
        p.setEmployee(emp);
        phoneRepository.save(p);
    }
    @Override
    public void removePhone(long employeeId, String phoneNo) {
        PhoneId pid = new PhoneId(employeeId, phoneNo);
        phoneRepository.deleteById(pid);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(Long deptId) {
        return employeeRepository.findByRole_Department_DeptId(deptId);
    }

    @Override
    public void changeEmployeeRole(Long managerId, Long employeeId, String newRoleName) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!manager.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER")) {
            throw new RuntimeException("Not a manager!");
        }
        Long deptId = manager.getRole().getDepartment().getDeptId();
        if (!emp.getRole().getDepartment().getDeptId().equals(deptId)) {
            throw new RuntimeException("Employee not in your department!");
        }
        Role newRole = roleRepository.findByDepartment_DeptId(deptId)
                .stream().filter(r -> r.getId().getRoleName().equals(newRoleName))
                .findFirst().orElseThrow(() -> new RuntimeException("Role not found!"));
        emp.setRole(newRole);
        employeeRepository.save(emp);
    }
}