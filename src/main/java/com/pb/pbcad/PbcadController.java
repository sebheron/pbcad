package com.pb.pbcad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PbcadController {

    @GetMapping("/")
    public String Test(Model model)
    {
        model.addAttribute("consoleLog", "CONSOLE LOG INFORMATION");
        return "index";
    }
}
