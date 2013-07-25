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

package org.jasig.portlet.courses.dao.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.jasig.portlet.courses.dao.ICourseOfferingDao;
import org.jasig.portlet.courses.model.catalog.xml.CourseList;
import org.jasig.portlet.courses.model.catalog.xml.CourseSection;
import org.jasig.portlet.courses.model.catalog.xml.Department;
import org.jasig.portlet.courses.model.catalog.xml.DepartmentList;
import org.jasig.portlet.courses.model.catalog.xml.FullCourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.catalog.xml.SchoolList;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class HttpClientCourseOfferingDaoImpl implements ICourseOfferingDao {

    private RestTemplate restTemplate;
    
    @Autowired(required = true)
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private Ehcache cache;
    
    @Autowired(required = true)
    public void setCache(Ehcache cache) {
        this.cache = cache;
    }

    protected final String SCHOOL_LIST_KEY = "schoolList";
    protected final String TERM_LIST_KEY = "termList";
    protected final String DEPARTMENT_LIST_KEY = "departmentList";
    protected final String COURSE_LIST_KEY = "courseList";

    @Value("http://localhost:8080/CoursesPortlet/data/terms/{school}.xml")
    private String termListUrl;
    
    public void setTermListUrl(String termListUrl) {
        this.termListUrl = termListUrl;
    }

    @Value("http://localhost:8080/CoursesPortlet/data/schools.xml")
    private String schoolListUrl;
    
    public void setSchoolListUrl(String schoolListUrl) {
        this.schoolListUrl = schoolListUrl;
    }

    @Value("http://localhost:8080/CoursesPortlet/data/departments/{school}/{term}.xml")
    private String departmentListUrl;
    
    public void setDepartmentListUrl(String departmentListUrl) {
        this.departmentListUrl = departmentListUrl;
    }

    @Value("http://localhost:8080/CoursesPortlet/data/courses/{school}/{term}/{department}.xml")
    private String courseListUrl;
    
    public void setCourseListUrl(String courseListUrl) {
        this.courseListUrl = courseListUrl;
    }

    @Value("http://localhost:8080/CoursesPortlet/data/courses/{school}/{term}/{department}/{course}.xml")
    private String courseUrl;

    public void setCourseUrl(String courseUrl) {
        this.courseUrl = courseUrl;
    }

    @Override
    public TermList getTerms(String schoolCode) {
        final String cacheKey = TERM_LIST_KEY.concat(schoolCode);
        final Element element = cache.get(cacheKey);
        if (element != null) {
            return (TermList) element.getObjectValue();
        }
        
        else {
            final TermList terms = restTemplate.getForObject(termListUrl,
                    TermList.class, Collections.singletonMap("school", schoolCode));
            cache.put(new Element(cacheKey, terms));
            return terms;
        }
        
    }

    @Override
    public Term getCurrentTerm(String schoolCode) {
        final TermList termList = getTerms(schoolCode);
        for (Term term : termList.getTerms()) {
            if (term.isCurrent()) {
                return term;
            }
        }
        
        if (termList.getTerms().size() > 0) {
            return termList.getTerms().get(0);
        }

        return null;
    }

    @Override
    public SchoolList getSchools() {
        final Element element = cache.get(SCHOOL_LIST_KEY);
        if (element != null) {
            return (SchoolList) element.getObjectValue();
        }
        
        else {
            final SchoolList schools = restTemplate.getForObject(schoolListUrl, SchoolList.class);
            cache.put(new Element(SCHOOL_LIST_KEY, schools));
            return schools;
        }
    }

    @Override
    public School getSchool(String schoolCode) {
        final SchoolList schoolList = getSchools();
        for (School school : schoolList.getSchools()) {
            if (school.getCode().equals(schoolCode)) {
                return school;
            }
        }
        
        return null;
    }

    @Override
    public DepartmentList getDepartments(String schoolCode, String termCode) {
        final String cacheKey = DEPARTMENT_LIST_KEY.concat(schoolCode).concat(termCode);
        final Element element = cache.get(cacheKey);
        if (element != null) {
            return (DepartmentList) element.getObjectValue();
        }
        
        else {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("school", schoolCode);
            params.put("term", termCode);
            final DepartmentList departments = restTemplate.getForObject(departmentListUrl, DepartmentList.class, params);;
            cache.put(new Element(cacheKey, departments));
            return departments;
        }
    }

    @Override
    public Department getDepartment(String schoolCode, String departmentCode, String termCode) {
        final DepartmentList departmentList = getDepartments(schoolCode, termCode);
        for (Department department : departmentList.getDepartments()) {
            if (department.getCode().equals(departmentCode)) {
                return department;
            }
        }

        return null;
    }

    @Override
    public CourseList getCourseOfferings(
            String schoolCode, String departmentCode, String termCode) {
        final String cacheKey = COURSE_LIST_KEY.concat(schoolCode).concat(departmentCode).concat(termCode);
        final Element element = cache.get(cacheKey);
        if (element != null) {
            return (CourseList) element.getObjectValue();
        }
        
        else {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("school", schoolCode);
            params.put("term", termCode);
            params.put("department", departmentCode);
            final CourseList courses = restTemplate.getForObject(courseListUrl, CourseList.class, params);
            cache.put(new Element(cacheKey, courses));
            return courses;
        }
    }

    @Override
    public FullCourseOffering getCourseOffering(String courseCode,
            String termCode) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("term", termCode);
        params.put("course", courseCode);
        final FullCourseOffering course = restTemplate.getForObject(courseUrl, FullCourseOffering.class, params);
        return course;
    }

    @Override
    public CourseSection getCourseSectionOffering(String courseCode,
            String sectionCode, String termCode) {
        final FullCourseOffering course = getCourseOffering(courseCode, termCode);
        if (course != null) {
            for (CourseSection section : course.getCourseSections()) {
                if (section.getCode().equals(sectionCode)) {
                    return section;
                }
            }
        }
        
        return null;
    }
    

}
