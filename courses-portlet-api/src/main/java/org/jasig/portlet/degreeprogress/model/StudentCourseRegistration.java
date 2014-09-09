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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jasig.portlet.degreeprogress.model.xml.Course;
import org.jasig.portlet.degreeprogress.model.xml.Grade;
import org.jasig.portlet.degreeprogress.model.xml.Semester;


public class StudentCourseRegistration {

	private boolean completed;
	private Course course;
	private Semester semester;
	private Grade grade;
	private String source;
	private double credits;

	public StudentCourseRegistration() { }
	
	public StudentCourseRegistration(Course course, Grade grade) {
	    this.course = course;
	    this.grade = grade;
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getCredits() {
		return credits;
	}

	public void setCredits(double credits) {
		this.credits = credits;
	}
	
	public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append(this.course)
            .append(this.grade)
            .append(this.credits)
            .append(this.completed)
            .toString();
	}

}
