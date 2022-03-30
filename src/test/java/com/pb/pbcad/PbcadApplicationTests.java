package com.pb.pbcad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PbcadApplicationTests {

    private VPRObject parsingObject;

    @BeforeAll
    void setup() {
        parsingObject = new VPRObject();
    }

    @Test
    void testComponentAdding() {
    }
}
