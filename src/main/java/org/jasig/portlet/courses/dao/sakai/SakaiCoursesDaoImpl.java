package org.jasig.portlet.courses.dao.sakai;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.dao.sakai.json.SakaiSessionResponse;
import org.jasig.portlet.courses.model.sakai.xml.Refs;
import org.jasig.portlet.courses.model.sakai.xml.Site;
import org.jasig.portlet.courses.model.wrapper.CourseSummaryWrapper;
import org.jasig.portlet.courses.model.wrapper.CourseWrapper;
import org.jasig.portlet.courses.model.xml.Announcement;
import org.jasig.portlet.courses.model.xml.Course;
import org.jasig.portlet.courses.model.xml.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class SakaiCoursesDaoImpl implements ICoursesDao {

    private RestTemplate restTemplate;
    
    @Autowired(required = true)
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CourseSummaryWrapper getSummary(PortletRequest request) {
        Refs refs = getSiteData(request);
        CourseSummaryWrapper courses = new CourseSummaryWrapper();
        for (Site site : refs.getRef0().getData().getSiteCollection().getSite()) {
            Course course = new CourseWrapper();
            course.setCode(site.getId());
            course.setTitle(site.getEntityTitle());
            course.setUrl(site.getEntityURL());
            Instructor instructor = new Instructor();
            instructor.setFullName(site.getSiteOwner().getUserDisplayName());
            instructor.setIdentifier(site.getSiteOwner().getUserId());
            instructor.setAbbreviation(site.getSiteOwner().getUserId());
            course.getInstructors().add(instructor);
            courses.getCourses().add(course);
        }
        
        for (org.jasig.portlet.courses.model.sakai.xml.Announcement announcement : refs.getRef1().getData().getAnnouncementCollection().getAnnouncement()) {
            Announcement ann = new Announcement();
            ann.setTitle(announcement.getTitle());
            ann.setDescription(announcement.getBody());
            ann.setUrl(announcement.getEntityURL());
            for (Course course : courses.getCourses()) {
                if (course.getTitle().equals(announcement.getSiteTitle())) {
                    course.getAnnouncements().add(ann);
                }
            }
        }
        
        return courses;
    }
    
    protected Refs getSiteData(PortletRequest request) {
        String sessionId = getAuthenticatedSession(request);
        Map<String, String> vars = Collections.singletonMap("sessionId", sessionId);
        return restTemplate.getForObject("http://localhost:8180/direct/batch.xml?_refs=/direct/site.xml&_refs=/direct/announcement/user.xml&_sessionId={sessionId}", Refs.class, vars);
    }

    protected String getAuthenticatedSession(PortletRequest request) {
        Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
        
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("username", userInfo.get("user.login.id"));
        vars.put("password", userInfo.get("password"));
        
        return restTemplate.postForObject("http://localhost:8180/direct/session.json?_username={username}&_password={password}", null, String.class, vars);
    }

}
