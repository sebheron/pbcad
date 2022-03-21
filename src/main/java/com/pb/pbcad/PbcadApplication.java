package com.pb.pbcad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.pb.pbcad.*" })
public class PbcadApplication {
    public static void main(String[] args) {
        SpringApplication.run(PbcadApplication.class, args);
    }
}
