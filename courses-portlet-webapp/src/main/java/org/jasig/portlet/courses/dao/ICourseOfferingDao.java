package org.jasig.portlet.courses.dao;

import java.util.List;

import org.jasig.portlet.courses.model.catalog.xml.CourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.CourseSection;
import org.jasig.portlet.courses.model.catalog.xml.Department;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.xml.Term;

public interface ICourseOfferingDao {
    
    public List<Term> getTerms();
    
    public Term getCurrentTerm();

    public List<School> getSchools();
    
    public School getSchool(String schoolCode);
    
    public List<Department> getDepartments(String schoolCode);
    
    public Department getDepartment(String schoolCode, String departmentCode);
    
    public List<CourseOffering> getCourseOfferings(String schoolCode, String departmentCode, String termCode);
    
    public CourseOffering getCourseOffering(String courseCode, String termCode);
    
    public CourseSection getCourseSectionOffering(String courseCode, String sectionCode, String termCode);
    
    public List<CourseOffering> searchForCourse(String query, String termCode);
    
}
