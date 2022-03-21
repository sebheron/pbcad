package com.pb.pbcad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PbcadController {

    private final PbcadService pbcadService;

    public PbcadController() {
        this.pbcadService = new PbcadService();
    }

    @GetMapping("/")
    public String Test(@RequestParam(name="ds", required=false, defaultValue="") String ds, Model model)
    {
        model.addAttribute("ds", ds);
        model.addAttribute("log", this.pbcadService.InterpretDisplayString(ds));
        return "index";
    }
}
