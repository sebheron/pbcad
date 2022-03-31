package com.pb.pbcad;

import org.COPASI.*;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.springframework.stereotype.Repository;
import org.virtualparts.VPRException;
import org.virtualparts.ws.client.VPRWebServiceClient;

import javax.ws.rs.client.WebTarget;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class ParsingService {

    private WebTarget target;
    private CADObject parsingObject;

    public ParsingService() {
        target = VPRWebServiceClient.getVPRWebServiceTarget("http://virtualparts.org/virtualparts-ws/webapi");
        parsingObject = new CADObject();
    }

    public String InterpretDisplayString(String displayString) {
        if (displayString.equals("")) return "Output";
        try {
            SBOLDocument sbolDesign = parsingObject.Parse(displayString);
            SBMLDocument sbmlDoc = VPRWebServiceClient.getModel(target, sbolDesign);
            SBMLWriter.write(sbmlDoc, "cad.xml", ' ', (short) 2);
            String output = this.RunSimulation();
            Files.deleteIfExists(Path.of("cad.xml"));
            Files.deleteIfExists(Path.of("report.txt"));
            return output;
        }
        catch (SBOLValidationException e) {
            return "Error occurred with SBOL validation, check design string.";
        }
        catch (VPRException e) {
            return "Error occurred with VPR, check design string.";
        }
        catch (XMLStreamException e) {
            return "Error occurred with VPR in SBOL to SBML conversion.";
        }
        catch (IOException e) {
            return "SBML File error occurred.";
        }
        catch (PBSyntaxException e) {
            return e.getMessage();
        }
    }

    private String RunSimulation()
    {
        CDataModel dataModel = CRootContainer.addDatamodel();
        try
        {
            dataModel.importSBML("cad.xml");
            CModel model = dataModel.getModel();
            CReportDefinitionVector reports = dataModel.getReportDefinitionList();
            CReportDefinition report = CreateReportDef(model, reports);
            CTrajectoryTask trajectoryTask = (CTrajectoryTask)dataModel.getTask("Time-Course");
            trajectoryTask.setMethodType(CTaskEnum.Method_deterministic);
            trajectoryTask.getProblem().setModel(dataModel.getModel());
            trajectoryTask.setScheduled(true);
            trajectoryTask.getReport().setReportDefinition(report);
            trajectoryTask.getReport().setTarget("report.txt");
            trajectoryTask.getReport().setAppend(false);

            CTrajectoryProblem problem = (CTrajectoryProblem)trajectoryTask.getProblem();

            problem.setStepNumber(100);
            dataModel.getModel().setInitialTime(0.0);
            problem.setDuration(10);
            problem.setTimeSeriesRequested(true);
            CTrajectoryMethod method = (CTrajectoryMethod)trajectoryTask.getMethod();

            CCopasiParameter parameter = method.getParameter("Absolute Tolerance");
            parameter.setDblValue(1.0e-12);

            if (trajectoryTask.processWithOutputFlags(true, (int)CCopasiTask.OUTPUT_UI)) {
                return Files.readString(Path.of("report.txt"));
            }
            return "Error occured collecting results for simulation";
        }
        catch (Exception ex)
        {
            return "Error occured with COPASI when running the simulation. It's likely the design string entered is incorrectly setup.";
        }
    }

    private static CReportDefinition CreateReportDef(CModel model, CReportDefinitionVector reports)
    {
        CReportDefinition report = reports.createReportDefinition("Report", "Output for timecourse");
        report.setTaskType(CTaskEnum.Task_timeCourse);
        report.setIsTable(false);
        report.setSeparator(new CCopasiReportSeparator(", "));

        ReportItemVector header = report.getHeaderAddr();
        ReportItemVector body = report.getBodyAddr();

        body.add(new CRegisteredCommonName(model.getObject(new CCommonName("Reference=Time")).getCN().getString()));
        body.add(new CRegisteredCommonName(report.getSeparator().getCN().getString()));
        header.add(new CRegisteredCommonName(new CDataString("time").getCN().getString()));
        header.add(new CRegisteredCommonName(report.getSeparator().getCN().getString()));

        int i, iMax =(int) model.getMetabolites().size();
        for (i = 0;i < iMax;++i)
        {
            CMetab metab = model.getMetabolite(i);
            if (metab.getStatus() != CModelEntity.Status_FIXED)
            {
                body.add(new CRegisteredCommonName(metab.getObject(new CCommonName("Reference=Concentration")).getCN().getString()));
                header.add(new CRegisteredCommonName(new CDataString(metab.getSBMLId()).getCN().getString()));
                if(i!=iMax-1)
                {
                    body.add(new CRegisteredCommonName(report.getSeparator().getCN().getString()));
                    header.add(new CRegisteredCommonName(report.getSeparator().getCN().getString()));
                }

            }
        }
        return report;
    }
}