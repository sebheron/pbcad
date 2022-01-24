package com.pb.pbcad;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PbcadController {

    @RequestMapping
    public String Test()
    {
        return "teststring!";
    }
}
