package com.pb.pbcad;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PbcadController {

    @RequestMapping("/")
    public String Test()
    {
        return "index";
    }
}
