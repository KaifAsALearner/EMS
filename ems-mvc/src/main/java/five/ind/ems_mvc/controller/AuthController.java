package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.dto.EmployeeDto;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {

    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Home page
    @GetMapping("/index")
    public String home() {
        return "index";
    }

    // Login page
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        EmployeeDto employee = new EmployeeDto();
        model.addAttribute("employee", employee);
        return "register";
    }

    // Handle registration form submission
    @PostMapping("/register/save")
    public String registerEmployee(@Valid @ModelAttribute("employee") EmployeeDto employeeDto,
                                   BindingResult result,
                                   Model model) {
        Employee existing = employeeService.findByUsername(employeeDto.getUsername());
        if (existing != null) {
            result.rejectValue("Username", null,
                    "An account already exists with this username.");
        }

        if (result.hasErrors()) {
            model.addAttribute("employee", employeeDto);
            return "register";
        }

        employeeService.saveEmployee(employeeDto);
        return "redirect:/register?success";
    }

    // List all registered employees (ADMIN access only)
    @GetMapping("/employees")
    public String listRegisteredEmployees(Model model) {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "dashboard";
    }
}
