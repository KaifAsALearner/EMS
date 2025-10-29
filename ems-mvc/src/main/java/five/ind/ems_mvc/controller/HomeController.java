package five.ind.ems_mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToIndex(){
        return "redirect:/index";
    }

    // Index page
    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
}
