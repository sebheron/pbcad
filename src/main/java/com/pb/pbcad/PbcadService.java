package com.pb.pbcad;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;
import org.sbolstandard.core2.SBOLDocument;
import org.springframework.stereotype.Repository;
import org.virtualparts.sbol.SVPWriteHandler;
import org.virtualparts.ws.client.VPRWebServiceClient;

import javax.ws.rs.client.WebTarget;

import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class PbcadService {
    public String InterpretDisplayString(String displayString) {
        if (displayString.equals("")) return "Output";

        try {
            SBOLDocument sbolDesign = SVPWriteHandler.convertToSBOL(displayString, "tu");
            WebTarget target = VPRWebServiceClient.getVPRWebServiceTarget("https://virtualparts.org/rdf4j-server/repositories/vpr28");
            SBMLDocument sbmlDoc = VPRWebServiceClient.getModel(target, sbolDesign);
            SBMLWriter.write(sbmlDoc, "cad.xml", ' ', (short) 2);
            return Files.readString(Path.of("cad.xml"));
        }
        catch (Exception e) {
            return "Error occurred, check design string.";
        }
    }
}
