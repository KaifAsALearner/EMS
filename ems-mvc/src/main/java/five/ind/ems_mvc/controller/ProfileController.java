package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.dto.EmployeeDto;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import five.ind.ems_mvc.entity.Employee;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {
    private final EmployeeService employeeService;

    public ProfileController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/profile")
    public String profilePage(Principal principal, Model model) {
        String username = principal.getName();
        Employee employee = employeeService.findByUsername(username);
        List<String> emails = employeeService.findEmails(employee.getEmpId()); // your earlier method

        model.addAttribute("employee", employee);
        model.addAttribute("emails", emails);

        return "profile";
    }

    @GetMapping("/employees")
    public String listRegisteredEmployees(Model model) {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "dashboard";
    }

}
