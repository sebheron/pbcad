package com.pb.pbcad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PbcadController {

    private final ParsingService parsingService;

    @Autowired
    public PbcadController(ParsingService parsingService) {
        this.parsingService = parsingService;
    }

    @GetMapping("/")
    public String RunSim(@RequestParam(name="ds", required=false, defaultValue="") String ds, Model model)
    {
        model.addAttribute("ds", ds);
        model.addAttribute("log", this.parsingService.InterpretDisplayString(ds));
        return "index";
    }
}