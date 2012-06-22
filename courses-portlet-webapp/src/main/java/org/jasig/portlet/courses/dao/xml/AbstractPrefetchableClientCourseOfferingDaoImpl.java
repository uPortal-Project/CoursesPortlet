package org.jasig.portlet.courses.dao.xml;

import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.jasig.portlet.courses.dao.ICourseOfferingDao;
import org.jasig.portlet.courses.model.catalog.xml.CourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.CourseOfferingList;
import org.jasig.portlet.courses.model.catalog.xml.Department;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.catalog.xml.Term;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractPrefetchableClientCourseOfferingDaoImpl implements ICourseOfferingDao {

    protected final String TERM_LIST_KEY = "terms";
    protected final String SCHOOL_LIST_KEY = "schools";
    protected final String SCHOOL_KEY = "school.";
    protected final String COURSE_LIST_KEY = "courseList.";
    protected final String COURSE_KEY = "course.";
    
    protected Cache cache;
    
    public void setCache(Cache cache) {
        this.cache = cache;
    }
    
    protected abstract void fetchCourseOfferings();
    
    protected String getSchoolCacheKey(String schoolCode) {
        return SCHOOL_KEY.concat(schoolCode);
    }
    
    protected String getCourseListCacheKey(String schoolCode, String departmentCode, String termCode) {
        return COURSE_LIST_KEY.concat(schoolCode).concat(".").concat(departmentCode).concat(".").concat(termCode);
    }
    
    protected String getCourseCacheKey(String courseCode, String termCode) {
        return COURSE_KEY.concat(courseCode).concat(".").concat(termCode);
    }
    
    @Override
    public List<School> getSchools() {
        fetchCourseOfferings();
        final Element cached = cache.get(SCHOOL_LIST_KEY);
        if (cached != null) {
            @SuppressWarnings("unchecked")
            final List<School> schools = (List<School>) cached.getValue();
            return schools;
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    public School getSchool(final String schoolCode) {
        final List<School> schools = getSchools();
        for (final School school : schools) {
            if (school.getCode().equals(schoolCode)) {
                return school;
            }
        }
        
        return null;
    }

    @Override
    public List<Department> getDepartments(final String schoolCode) {
        final String schoolCacheKey = getSchoolCacheKey(schoolCode);
        final Element cached = cache.get(schoolCacheKey);
        if (cached != null) {
            final School school = (School) cached.getValue();
            return school.getDepartments();
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    public Department getDepartment(final String schoolCode, final String departmentCode) {
        final List<Department> departments = getDepartments(schoolCode);
        for (final Department department : departments) {
            if (department.getCode().equals(departmentCode)) {
                return department;
            }
        }
        return null;
    }

    @Override
    public List<CourseOffering> getCourseOfferings(final String schoolCode, final String departmentCode, final String termCode) {
        final String courseListCacheKey = getCourseListCacheKey(schoolCode, departmentCode, termCode);
        final Element cached = cache.get(courseListCacheKey);
        if (cached != null) {
            final CourseOfferingList list = (CourseOfferingList) cached.getValue();
            return list.getCourseOfferings();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CourseOffering> searchForCourse(String query, String termCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Term> getTerms() {
        final Element cached = cache.get(TERM_LIST_KEY);
        if (cached != null) {
            @SuppressWarnings("unchecked")
            final List<Term> terms = (List<Term>) cached.getValue();
            return terms;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Term getCurrentTerm() {
        final List<Term> terms = getTerms();
        for (final Term term : terms) {
            if (term.isCurrent()) {
                return term;
            }
        }
        
        if (terms.size() > 0) {
            return terms.get(0);
        }
        
        return null;
    }

    @Override
    public CourseOffering getCourseOffering(String courseCode, String termCode) {
        final String courseCacheKey = getCourseCacheKey(courseCode, termCode);
        final Element cached = cache.get(courseCacheKey);
        if (cached != null) {
            final CourseOffering course = (CourseOffering) cached.getValue();
            return course;
        } else {
            return null;
        }
    }

}
