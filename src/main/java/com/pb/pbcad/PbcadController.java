package com.pb.pbcad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PbcadController {

    @GetMapping("/")
    public String Test(@RequestParam(name="ds", required=false, defaultValue="") String ds, Model model)
    {
        model.addAttribute("ds", ds);
        model.addAttribute("log", "CONSOLE LOG INFORMATION");
        return "index";
    }
}
