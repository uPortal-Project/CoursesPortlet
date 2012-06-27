package org.jasig.portlet.courses.dao.xml;

import java.util.List;

import net.sf.ehcache.Element;

import org.jasig.portlet.courses.model.catalog.xml.CourseCatalog;
import org.jasig.portlet.courses.model.catalog.xml.CourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.CourseOfferingList;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.xml.TermList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

public class HttpClientCourseOfferingDaoImpl extends AbstractPrefetchableClientCourseOfferingDaoImpl {

    @Autowired(required = true)
    private RestTemplate restTemplate;
    
    private String url = "http://localhost:8080/CoursesPortlet/data/course-catalog.xml";
    
    @Scheduled(fixedRate=90000)
    protected void fetchCourseOfferings() {

        final CourseCatalog catalog = restTemplate.getForObject(url, CourseCatalog.class);
        
        // cache the school list
        final List<School> schools = catalog.getSchools();
        cache.put(new Element(SCHOOL_LIST_KEY, schools));
        
        // cache the term list
        final TermList terms = catalog.getTermList();
        cache.put(new Element(TERM_LIST_KEY, terms));
        
        // cache each individual school
        for (final School school : schools) {
            final String schoolCacheKey = getSchoolCacheKey(school.getCode());
            cache.put(new Element(schoolCacheKey, school));
        }

        // cache each course offering list
        final List<CourseOfferingList> courseLists = catalog.getCourseOfferingLists();
        for (final CourseOfferingList list : courseLists) {
            final String listCacheKey = getCourseListCacheKey(list.getSchool(), list.getDepartment(), list.getTerm());
            cache.put(new Element(listCacheKey, list));

            // cache each course offering
            for (final CourseOffering course : list.getCourseOfferings()) {
                final String courseCacheKey = getCourseCacheKey(course.getCode(), list.getTerm());
                cache.put(new Element(courseCacheKey, course));
            }
        }
    }

}
