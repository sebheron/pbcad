package com.pb.pbcad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PbcadController {

    @RequestMapping("/")
    public String Display()
    {
        //model.addAttribute("consoleOutput", "This is the console output!");
        return "display";
    }
}