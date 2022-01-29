package com.pb.pbcad;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PbcadController {

    @RequestMapping("/")
    public String Home()
    {
        //model.addAttribute("consoleOutput", "This is the console output!");
        return "index";
    }
}