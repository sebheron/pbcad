package com.pb.pbcad;

import org.sbolstandard.core2.*;
import org.virtualparts.*;
import org.virtualparts.sbol.*;

import java.util.*;

public class VPRObject {
    private static Set<String> ComponentTypes = Set.of(
            "prom",
            "rbs",
            "cds",
            "ter",
            "op",
            "shim",
            "eng"
    );

    private static Set<String> InteractionTypes = Set.of(
            "$rep",
            "$act",
            "$trans",
            "$bind",
            "$form",
            "$phos",
            "$dephos"
    );

    private Map<String, String> Components;
    private Map<String, List<String>> Interactions;

    public VPRObject() {
        this.Components = new LinkedHashMap<>();
        this.Interactions = new LinkedHashMap<>();
    }

    public SBOLDocument Parse(String designString) throws PBSyntaxException, SBOLValidationException, VPRException {
        this.Components.clear();
        this.Interactions.clear();
        addInformation(designString);
        SBOLDocument document = SVPWriteHandler.convertToSBOL(getVPRDesignString(), "pbcad");
        if (this.Interactions.size() <= 0) return document;
        ModuleDefinition moduleDef = document.createModuleDefinition("design_module");
        for (Map.Entry<String, List<String>> entry : this.Interactions.entrySet()) {
            String type = entry.getKey();
            List<String> vals = entry.getValue();
            List<ComponentDefinition> definitions = new ArrayList<>();
            for (String val : vals) {
                definitions.add(document.getComponentDefinition(val, "1"));
            }
            try {
                if (type == "$rep") {
                    if (definitions.size() > 2) throw new PBSyntaxException("Too many component NAMES specified in interaction definition.");
                    SBOLInteraction.createPromoterRepression(moduleDef, definitions.get(0), definitions.get(1));
                } else if (type == "$act") {
                    if (definitions.size() > 2) throw new PBSyntaxException("Too many component NAMES specified in interaction definition.");
                    SBOLInteraction.createPromoterInduction(moduleDef, definitions.get(0), definitions.get(1));
                } else if (type == "$trans") {
                    if (definitions.size() > 2) throw new PBSyntaxException("Too many component NAMES specified in interaction definition.");
                    SBOLInteraction.createTranslationInteraction(moduleDef, definitions.get(0), definitions.get(1));
                } else if (type == "$blind") {
                    if (definitions.size() > 3) throw new PBSyntaxException("Too many component NAMES specified in interaction definition.");
                    SBOLInteraction.createDNABinding(moduleDef, definitions.get(0), definitions.get(1), definitions.get(2));
                } else if (type == "$form") {
                    if (definitions.size() > 3) throw new PBSyntaxException("Too many component NAMES specified in interaction definition.");
                    SBOLInteraction.createComplexFormation(moduleDef, definitions.get(0), definitions.get(1), definitions.get(2));
                } else if (type == "$phos") {
                    if (definitions.size() > 2) throw new PBSyntaxException("Too many component NAMES specified in interaction definition.");
                    SBOLInteraction.createPhosphorylationInteraction(moduleDef, definitions.get(0), definitions.get(1));
                } else if (type == "$dephos") {
                    if (definitions.size() > 1) throw new PBSyntaxException("Too many component NAMES specified in interaction definition.");
                    SBOLInteraction.createAutoDephosphorylationInteraction(moduleDef, definitions.get(0));
                }
            }
            catch (IndexOutOfBoundsException e) {
                throw new PBSyntaxException("Not enough component NAMES specified in interaction definition.");
            }
        }
        return document;
    }

    private String getVPRDesignString() {
        int i = 0;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : this.Components.entrySet()) {
            builder.append(entry.getValue() + ":" + entry.getKey());
            if (i < this.Components.size() - 1) {
                builder.append(";");
            }
            i++;
        }
        return builder.toString();
    }

    private void addInformation(String designString) throws PBSyntaxException {
        //Split comma seperated string.
        //Use regex to include different grammatical choices.
        //All these will return [A,B,C].
        //A, B, C
        //A, B , C
        //A,B,C
        String[] components = designString.trim().split("\\s*,\\s*");
        //Loop through firstly to add each component.
        for (String component : components) {
            if (component.equals("")) continue;
            if (!component.contains("$")) addComponent(component);
        }
        //Loop through secondly to add each interaction.
        for (String component : components) {
            if (component.equals("")) continue;
            if (component.contains("$")) addInteraction(component);
        }
    }

    private void addComponent(String componentString) throws PBSyntaxException {
        //Simple space split, by this point we've parsed to pure components.
        String[] typeAndName = componentString.split(" ");
        //Firstly we need to confirm the length.
        if (typeAndName.length != 2) {
            buildComponentException(typeAndName);
        }
        //Now we can make checks to see if the component type is valid.
        buildTypeException(typeAndName[0]);
        //Finally, we're going to make sure the name isn't already used.
        buildNameUsedException(typeAndName[1]);
        //Add component.
        this.Components.put(typeAndName[0], typeAndName[1]);
    }

    private void addInteraction(String interactionString) throws PBSyntaxException {
        //Simple space split.
        String[] interactionInformation = interactionString.split(" ");
        //Firstly we need to confirm that we have a valid interaction type.
        buildInteractException(interactionInformation[0]);
        //Then we need to make sure all the names used exist.
        List<String> names = new ArrayList<>();
        for (int i = 1; i < interactionInformation.length; i++) {
            buildNameExistsException(interactionInformation[i]);
            names.add(interactionInformation[i]);
        }
        //Add interaction.
        this.Interactions.put(interactionInformation[0], names);
    }

    /*
    Length confirmation for individual components added.
    This method is called when the correct length of 2 for a split string is not achieved.
    By breaking down exception building in this manor we can describe in detail syntax issues.
     */
    private void buildComponentException(String[] typeAndName) throws PBSyntaxException {
        if (typeAndName.length > 2) {
            throw new PBSyntaxException("Too many arguments in component declaration.");
        } else {
            try {
                buildTypeException(typeAndName[0]);
            } catch (PBSyntaxException e) {
                throw new PBSyntaxException("Missing component TYPE in component declaration.");
            }
            throw new PBSyntaxException("Missing component NAME in component declaration.");
        }
    }

    /*
    Check if the component type is correct.
    Called to determine whether the component type exists and is correct.
     */
    private void buildTypeException(String type) throws PBSyntaxException {
        if (!VPRObject.ComponentTypes.contains(type.toLowerCase())) {
            throw new PBSyntaxException("Unrecognised component TYPE in component declaration.");
        }
    }

    /*
    Check if the interaction type is correct.
    Called to determine whether the interaction type exists and is correct.
     */
    private void buildInteractException(String interaction) throws PBSyntaxException {
        if (!VPRObject.InteractionTypes.contains(interaction.toLowerCase())) {
            throw new PBSyntaxException("Unrecognised interaction TYPE in component declaration.");
        }
    }

    /*
    Check if the components name isn't used.
    Called to determine whether the name for the component defined is unused and able to be used.
    Also makes sure the name isn't a keyword.
     */
    private void buildNameUsedException(String name) throws PBSyntaxException {
        if (this.Components.containsKey(name)) {
            throw new PBSyntaxException("Component NAME is already defined in scope.");
        }
        if (VPRObject.ComponentTypes.contains(name) || VPRObject.InteractionTypes.contains(name)) {
            throw new PBSyntaxException("Component NAME cannot be the same as a predefined TYPE.");
        }
    }

    /*
    Check if the components name isn't used.
    Called to determine whether the name for the component defined is unused and able to be used.
     */
    private void buildNameExistsException(String name) throws PBSyntaxException {
        if (!this.Components.containsKey(name)) {
            throw new PBSyntaxException("Component NAME does not exist in scope.");
        }
    }
}
