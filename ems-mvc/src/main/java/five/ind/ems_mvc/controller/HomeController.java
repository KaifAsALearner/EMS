package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final EmployeeService employeeService;

    public  HomeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/")
    public String redirectToIndex(){
        return "redirect:/index";
    }

    // Index page
    @GetMapping("/index")
    public String indexPage(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            Employee user = employeeService.findByUsername(username);
            model.addAttribute("loggedIn", true);
            model.addAttribute("currentUser", user);
            boolean isManager = user.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER");
            model.addAttribute("isManager", isManager);
        } else {
            model.addAttribute("loggedIn", false);
            model.addAttribute("isManager", false);
        }
        return "index";
    }
}
