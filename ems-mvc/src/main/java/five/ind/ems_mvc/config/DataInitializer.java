package five.ind.ems_mvc.config;

import five.ind.ems_mvc.entity.Department;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Role;
import five.ind.ems_mvc.entity.compositeId.RoleId;
import five.ind.ems_mvc.repository.DepartmentRepository;
import five.ind.ems_mvc.repository.EmployeeRepository;
import five.ind.ems_mvc.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring injects the beans we need via the constructor
    public DataInitializer(EmployeeRepository employeeRepository,
                           RoleRepository roleRepository,
                           DepartmentRepository departmentRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // We will create an admin user only if one doesn't already exist
        if (employeeRepository.findEmployeeByUsername("manager").isEmpty()) {
            System.out.println("🌱 Seeding initial data...");

            // 1. A Role needs a Department, so let's create one first.
            Department managementDept = departmentRepository.findByName("Management")
                    .orElseGet(() -> {
                        Department newDept = new Department();
                        newDept.setName("Management");
                        newDept.setDesc("General Management Department");
                        return departmentRepository.save(newDept);
                    });

            // 2. Now create the 'MANAGER' Role for that Department.
            // Remember, Role has a composite key (deptId, roleName)
            RoleId managerRoleId = new RoleId(managementDept.getDeptId(), "MANAGER");
            Role managerRole = roleRepository.findById(managerRoleId)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setId(managerRoleId);
                        newRole.setDepartment(managementDept);
                        newRole.setSeniority("Top Level");
                        return roleRepository.save(newRole);
                    });

            // 3. Finally, create the Employee with the encoded password and Role.
            Employee managerUser = new Employee();
            managerUser.setUsername("manager");
            managerUser.setPassword(passwordEncoder.encode("12345"));
            managerUser.setFName("Default");
            managerUser.setLName("Manager");
            managerUser.setGender("Other");
            managerUser.setDob(LocalDate.of(1990, 1, 1));
            managerUser.setHiredDate(LocalDate.now());
            managerUser.setRole(managerRole); // Assign the role here

            employeeRepository.save(managerUser);
            System.out.println("✅ Created user: 'manager' with password '12345'");
        }
    }
}
