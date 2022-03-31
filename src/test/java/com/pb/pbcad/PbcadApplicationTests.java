package com.pb.pbcad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PbcadApplicationTests {

    private CADObject parsingObject;

    @BeforeAll
    void setup() {
        parsingObject = new CADObject();
    }

    @Test
    void testComponentAdding() {
    }
}
