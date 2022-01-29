package com.pb.pbcad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PbcadController {

    @GetMapping("/")
    public String Display(Model model)
    {
        model.addAttribute("consoleOutput", "This is the console output!");
        return "display";
    }
}