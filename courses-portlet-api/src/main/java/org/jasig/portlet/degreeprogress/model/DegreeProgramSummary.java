package org.jasig.portlet.degreeprogress.model;

import java.util.ArrayList;
import java.util.List;

public class DegreeProgramSummary {

    private String program;
    private List<ProgramComponent> majors = new ArrayList<ProgramComponent>();
    private List<ProgramComponent> minors = new ArrayList<ProgramComponent>();
    private List<ProgramComponent> concentrations = new ArrayList<ProgramComponent>();

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public List<ProgramComponent> getMajors() {
        return majors;
    }
    
    public void addMajor(ProgramComponent major) {
        this.majors.add(major);
    }

    public void addMinor(ProgramComponent minor) {
        this.minors.add(minor);
    }

    public void addConcentration(ProgramComponent concentration) {
        this.concentrations.add(concentration);
    }

    public void setMajors(List<ProgramComponent> major) {
        this.majors = major;
    }

    public List<ProgramComponent> getMinors() {
        return minors;
    }

    public void setMinors(List<ProgramComponent> minor) {
        this.minors = minor;
    }

    public List<ProgramComponent> getConcentrations() {
        return concentrations;
    }

    public void setConcentrations(List<ProgramComponent> concentration) {
        this.concentrations = concentration;
    }

}
