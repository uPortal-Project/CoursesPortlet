package org.jasig.portlet.degreeprogress.dao;

import org.jasig.portlet.degreeprogress.model.ProgramComponent;

public class ProgramInformation {

    private ProgramComponent degree;
    private ProgramComponent campus;
    private ProgramComponent level;
    private ProgramComponent college;

    public ProgramComponent getDegree() {
        return degree;
    }

    public void setDegree(ProgramComponent degree) {
        this.degree = degree;
    }

    public ProgramComponent getCampus() {
        return campus;
    }

    public void setCampus(ProgramComponent campus) {
        this.campus = campus;
    }

    public ProgramComponent getLevel() {
        return level;
    }

    public void setLevel(ProgramComponent level) {
        this.level = level;
    }

    public ProgramComponent getCollege() {
        return college;
    }

    public void setCollege(ProgramComponent college) {
        this.college = college;
    }

}
