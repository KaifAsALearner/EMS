package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.dto.EmployeeDto;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import five.ind.ems_mvc.entity.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Employee employee = employeeService.findByUsername(principal.getName());
        List<String> emails = employeeService.findEmails(employee.getEmpId());
        List<String> phones = employeeService.findPhones(employee.getEmpId());
        model.addAttribute("employee", employee);
        model.addAttribute("emails", emails);
        model.addAttribute("phones", phones);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Principal principal, Model model) {
        Employee employee = employeeService.findByUsername(principal.getName());
        List<String> emails = employeeService.findEmails(employee.getEmpId());
        List<String> phones = employeeService.findPhones(employee.getEmpId());
        model.addAttribute("employee", employee);
        model.addAttribute("emails", emails);
        model.addAttribute("phones", phones);
        return "edit-employee";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute Employee employee,
                                @RequestParam(required = false) String newPassword,
                                Principal principal, Model model) {
        Employee current = employeeService.findByUsername(principal.getName());
        employee.setEmpId(current.getEmpId());
        employee.setUsername(current.getUsername());

        try {
            if(newPassword != null && !newPassword.isBlank()) employee.setPassword(newPassword);
            employeeService.updateEmployee(employee, newPassword != null && !newPassword.isBlank());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            // reload edit page with error
            List<String> emails = employeeService.findEmails(employee.getEmpId());
            List<String> phones = employeeService.findPhones(employee.getEmpId());
            model.addAttribute("emails", emails);
            model.addAttribute("phones", phones);
            return "edit-employee";
        }
        return "redirect:/profile";
    }

    // Add/remove email and phone (uses List<String> version for simplicity)
    @PostMapping("/profile/email/add")
    public String addEmail(@RequestParam String email, Principal principal) {
        Employee employee = employeeService.findByUsername(principal.getName());
        employeeService.addEmail(employee.getEmpId(), email);
        return "redirect:/profile/edit";
    }

    @GetMapping("/profile/email/remove")
    public String removeEmail(@RequestParam String email, Principal principal) {
        Employee employee = employeeService.findByUsername(principal.getName());
        employeeService.removeEmail(employee.getEmpId(), email);
        return "redirect:/profile/edit";
    }

    @PostMapping("/profile/phone/add")
    public String addPhone(@RequestParam String phoneNo, Principal principal) {
        Employee employee = employeeService.findByUsername(principal.getName());
        employeeService.addPhone(employee.getEmpId(), phoneNo);
        return "redirect:/profile/edit";
    }

    @GetMapping("/profile/phone/remove")
    public String removePhone(@RequestParam String phoneNo, Principal principal) {
        Employee employee = employeeService.findByUsername(principal.getName());
        employeeService.removePhone(employee.getEmpId(), phoneNo);
        return "redirect:/profile/edit";
    }

    @GetMapping("/employees")
    public String listRegisteredEmployees(Model model) {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "dashboard";
    }

}
