package org.jasig.portlet.courses.dao;

import org.jasig.portlet.courses.model.catalog.xml.CourseList;
import org.jasig.portlet.courses.model.catalog.xml.CourseSection;
import org.jasig.portlet.courses.model.catalog.xml.Department;
import org.jasig.portlet.courses.model.catalog.xml.DepartmentList;
import org.jasig.portlet.courses.model.catalog.xml.FullCourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.catalog.xml.SchoolList;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;

public interface ICourseOfferingDao {
    
    public TermList getTerms(String schoolCode);
    
    public Term getCurrentTerm(String schoolCode);

    public SchoolList getSchools();
    
    public School getSchool(String schoolCode);
    
    public DepartmentList getDepartments(String schoolCode, String termCode);
    
    public Department getDepartment(String schoolCode, String departmentCode, String termCode);
    
    public CourseList getCourseOfferings(String schoolCode, String departmentCode, String termCode);
    
    public FullCourseOffering getCourseOffering(String courseCode, String termCode);
    
    public CourseSection getCourseSectionOffering(String courseCode, String sectionCode, String termCode);
    
}
