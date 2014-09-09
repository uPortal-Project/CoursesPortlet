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
package org.jasig.portlet.degreeprogress.dao;


public class WhatIfRequest {

    private int id;
    private String currentTerm;
    private String entryTerm;
    private String evaluationTerm;
    private String program;
    private int programRule;
    private String degree;
    private String level;
    private String campus;
    private String college;
    private String major;
    private String major2;
    private int majorRule;
    private String minor;
    private String concentration;
    private int majorRule2;
    private String minor2;
    private String concentration2;
    private String department;
    private String department2;
    
    //Daves Additions
    private int minorRule;
    private int minorRule2;
    private int concentrationRule;
    private int concentrationRule2;

    public int getMinorRule() {
		return minorRule;
	}

	public void setMinorRule(int minorRule) {
		this.minorRule = minorRule;
	}

	public int getMinorRule2() {
		return minorRule2;
	}

	public void setMinorRule2(int minorRule2) {
		this.minorRule2 = minorRule2;
	}

	public int getConcentrationRule() {
		return concentrationRule;
	}

	public void setConcentrationRule(int concentrationRule) {
		this.concentrationRule = concentrationRule;
	}

	public int getConcentrationRule2() {
		return concentrationRule2;
	}

	public void setConcentrationRule2(int concentrationRule2) {
		this.concentrationRule2 = concentrationRule2;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntryTerm() {
        return entryTerm;
    }

    public void setEntryTerm(String entryTerm) {
        this.entryTerm = entryTerm;
    }

    public String getEvaluationTerm() {
        return evaluationTerm;
    }

    public void setEvaluationTerm(String evaluationTerm) {
        this.evaluationTerm = evaluationTerm;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getProgramRule() {
        return programRule;
    }

    public void setProgramRule(int programRule) {
        this.programRule = programRule;
    }

    public int getMajorRule() {
        return majorRule;
    }

    public void setMajorRule(int majorRule) {
        this.majorRule = majorRule;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(String currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getMajor2() {
        return major2;
    }

    public void setMajor2(String major2) {
        this.major2 = major2;
    }

    public int getMajorRule2() {
        return majorRule2;
    }

    public void setMajorRule2(int majorRule2) {
        this.majorRule2 = majorRule2;
    }

    public String getMinor2() {
        return minor2;
    }

    public void setMinor2(String minor2) {
        this.minor2 = minor2;
    }

    public String getConcentration2() {
        return concentration2;
    }

    public void setConcentration2(String concentration2) {
        this.concentration2 = concentration2;
    }

    public String getDepartment2() {
        return department2;
    }

    public void setDepartment2(String department2) {
        this.department2 = department2;
    }

}
