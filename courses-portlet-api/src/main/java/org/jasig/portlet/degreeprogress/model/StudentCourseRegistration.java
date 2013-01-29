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
