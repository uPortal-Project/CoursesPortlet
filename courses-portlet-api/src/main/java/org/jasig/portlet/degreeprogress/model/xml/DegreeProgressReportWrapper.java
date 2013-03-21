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
package org.jasig.portlet.degreeprogress.model.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DegreeProgressReportWrapper {
    protected String program;
    protected String programText;
    private String catalogTerm;
    protected Double requiredOverallGpa;
    protected Double overallGpa;
    private Double requiredResidencyCredits;
    private Double residencyCredits;
    private Double requiredInstitutionalCredits;
    private Double institutionalCredits;
    protected Map<String, DegreeRequirementSection> sections = new LinkedHashMap<String, DegreeRequirementSection>();

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgramText() {
        return programText;
    }

    public void setProgramText(String programText) {
        this.programText = programText;
    }

    public String getCatalogTerm() {
        return catalogTerm;
    }

    public void setCatalogTerm(String catalogTerm) {
        this.catalogTerm = catalogTerm;
    }

    public Double getRequiredOverallGpa() {
        return requiredOverallGpa;
    }

    public void setRequiredOverallGpa(Double requiredOverallGpa) {
        this.requiredOverallGpa = requiredOverallGpa;
    }

    public Double getOverallGpa() {
        return overallGpa;
    }

    public void setOverallGpa(Double overallGpa) {
        this.overallGpa = overallGpa;
    }

    public Double getRequiredResidencyCredits() {
        return requiredResidencyCredits;
    }

    public void setRequiredResidencyCredits(Double requiredResidencyCredits) {
        this.requiredResidencyCredits = requiredResidencyCredits;
    }

    public Double getResidencyCredits() {
        return residencyCredits;
    }

    public void setResidencyCredits(Double residencyCredits) {
        this.residencyCredits = residencyCredits;
    }

    public Double getRequiredInstitutionalCredits() {
        return requiredInstitutionalCredits;
    }

    public void setRequiredInstitutionalCredits(Double requiredInstitutionalCredits) {
        this.requiredInstitutionalCredits = requiredInstitutionalCredits;
    }

    public Double getInstitutionalCredits() {
        return institutionalCredits;
    }

    public void setInstitutionalCredits(Double institutionalCredits) {
        this.institutionalCredits = institutionalCredits;
    }

    public Collection<DegreeRequirementSection> getSections() {
        return Collections.unmodifiableCollection(sections.values());
    }

    public void addSection(DegreeRequirementSection section) {
        this.sections.put(section.getKey(), section);
    }

    public DegreeRequirementSection getSection(String key) {
        return this.sections.get(key);
    }
}
