package com.pb.pbcad;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.ModuleDefinition;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.virtualparts.VPRException;
import org.virtualparts.sbol.SBOLInteraction;
import org.virtualparts.sbol.SVPWriteHandler;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PbcadApplicationTests {

    private CADObject parsingObject;

    @BeforeAll
    void setup() {
        parsingObject = new CADObject();
    }

    @Test
    void VPRWikiExample1Test() throws PBSyntaxException, VPRException, SBOLValidationException {
        SBOLDocument testDoc = parsingObject.Parse("prom prom1, rbs rbs1, cds cds1, ter ter1");
        SBOLDocument controlDoc = SVPWriteHandler.convertToSBOL("prom1:prom;rbs1:rbs;cds1:cds;ter1:ter","pbcad");
        Assertions.assertEquals(controlDoc.toString(), testDoc.toString());
    }

    @Test
    void VPRWikiExample2Test() throws PBSyntaxException, VPRException, SBOLValidationException {
        SBOLDocument testDoc = parsingObject.Parse("prom prom1, rbs rbs1, cds cds1, ter ter1, prot tf, $rep prom1 tf, $trans cds1 tf");

        SBOLDocument controlDoc = SVPWriteHandler.convertToSBOL("prom1:prom;rbs1:rbs;cds1:cds;ter1:ter","pbcad");

        ComponentDefinition tf = controlDoc.createComponentDefinition("tf", "1", ComponentDefinition.PROTEIN);
        ComponentDefinition prom = controlDoc.getComponentDefinition("prom1", "1");
        ComponentDefinition cds = controlDoc.getComponentDefinition("cds1", "1");

        ModuleDefinition moduleDef = controlDoc.createModuleDefinition("design_module");
        SBOLInteraction.createPromoterRepression(moduleDef, prom, tf);
        SBOLInteraction.createTranslationInteraction(moduleDef, cds, tf);

        Assertions.assertEquals(controlDoc.toString(), testDoc.toString());
    }

    @Test
    void VPRWikiExample3Test() {
    }

    /*
    Need to build a large test catalogue.
    Aim for 20 tests. No stragglers just meaningful tests.
     */
}
