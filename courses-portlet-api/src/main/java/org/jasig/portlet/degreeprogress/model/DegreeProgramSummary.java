/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
