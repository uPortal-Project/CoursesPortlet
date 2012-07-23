package org.jasig.portlet.courses.mvc.portlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.jasig.portlet.courses.dao.ICourseOfferingDao;
import org.jasig.portlet.courses.model.catalog.xml.CourseList;
import org.jasig.portlet.courses.model.catalog.xml.CourseSection;
import org.jasig.portlet.courses.model.catalog.xml.Department;
import org.jasig.portlet.courses.model.catalog.xml.DepartmentList;
import org.jasig.portlet.courses.model.catalog.xml.FullCourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.catalog.xml.SchoolList;
import org.jasig.portlet.courses.model.xml.CourseMeeting;
import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.mvc.IViewSelector;
import org.jasig.portlet.courses.service.IURLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("VIEW")
public class CourseCataloguePortletController {

    private ICourseOfferingDao dao;

    @Autowired(required = true)
    public void setCourseOfferingDao(ICourseOfferingDao dao) {
        this.dao = dao;
    }

    private IURLService urlService;
    
    @Autowired 
    public void setUrlService(IURLService urlService) {
        this.urlService = urlService;
    }
    
    private IViewSelector viewSelector;
    
    @Autowired(required = true)
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }

    @RequestMapping
    public String showMainView(final ModelMap model, final PortletRequest request) {
        final SchoolList schoolList = dao.getSchools();
        final List<School> schools = schoolList.getSchools();

        // if there's only one school configured, we don't need to offer users
        // a choice and should skip directly to displaying departments
        if (schools.size() == 1) {
            return showDepartments(null, schools.get(0).getCode(), model, request);
        } 
        
        else {
            model.addAttribute("schools", schools);

            final String view = viewSelector.isMobile(request) ? "course-catalog/schools-jQM" : "course-catalog/schools";
            return view;
        }
        
    }
    
    @RequestMapping(params = "action=departments")
    public String showDepartments(@RequestParam(required = false) String termCode, final @RequestParam String schoolCode, final ModelMap model, final PortletRequest request) {
        
        final TermList termList = dao.getTerms(schoolCode);
        model.addAttribute("terms", termList.getTerms());

        // if a term parameter was provided, update the term code in the session
        final PortletSession session = request.getPortletSession();
        if (termCode != null) {
            session.setAttribute("currentTerm", termCode);
        } 
        
        // otherwise use either the session-persisted term selection or 
        // the default term
        else {
            final String currentTerm = (String) session.getAttribute("currentTerm");
            if (currentTerm != null) {
                termCode = currentTerm;
            } else {
                final Term term = dao.getCurrentTerm(schoolCode);
                termCode = term.getCode();
                session.setAttribute("currentTerm", termCode);
            }
        }
        model.addAttribute("term", termCode);

        // get a list of departments
        final DepartmentList departmentList = dao.getDepartments(schoolCode, termCode);
        model.addAttribute("departments", departmentList.getDepartments());
        
        final School school = dao.getSchool(schoolCode);
        model.addAttribute("school", school);
        
        final String view = viewSelector.isMobile(request) ? "course-catalog/departments-jQM" : "course-catalog/departments";
        return view;
    }

    @RequestMapping(params = "action=courses")
    public String showCourses(final @RequestParam String termCode, final @RequestParam String schoolCode, final @RequestParam String departmentCode, final ModelMap model, final PortletRequest request) {

        final TermList termList = dao.getTerms(schoolCode);
        model.addAttribute("terms", termList.getTerms());

        final School school = dao.getSchool(schoolCode);
        model.addAttribute("school", school);
        
        final Department department = dao.getDepartment(schoolCode, departmentCode, termCode);
        model.addAttribute("department", department);

        model.addAttribute("termCode", termCode);

        // get a list of course offerings for the selected term and department
        final CourseList courseList = dao.getCourseOfferings(schoolCode, departmentCode, termCode);
        model.addAttribute("courses", courseList.getAbbreviatedCourseOfferings());

        final String view = viewSelector.isMobile(request) ? "course-catalog/courses-jQM" : "course-catalog/courses";
        return view;
    }

    @RequestMapping(params = "action=course")
    public String showCourse(final @RequestParam String schoolCode, final @RequestParam String departmentCode, final @RequestParam String courseCode, final ModelMap model, final PortletRequest request) {

        final PortletSession session = request.getPortletSession();
        final String termCode = (String) session.getAttribute("currentTerm");
        model.addAttribute("term", termCode);
        
        final FullCourseOffering course = dao.getCourseOffering(courseCode, termCode);
        model.addAttribute("course", course);

        final School school = dao.getSchool(schoolCode);
        model.addAttribute("school", school);
        
        final Department department = dao.getDepartment(schoolCode, departmentCode, termCode);
        model.addAttribute("department", department);

        final String view = viewSelector.isMobile(request) ? "course-catalog/course-jQM" : "course-catalog/course";
        return view;
    }
    
    @RequestMapping(params = "action=section")
    public String showCourse(final @RequestParam String schoolCode, final @RequestParam String departmentCode, final @RequestParam String courseCode, final @RequestParam String sectionCode, final ModelMap model, final PortletRequest request) {

        final PortletSession session = request.getPortletSession();
        final String termCode = (String) session.getAttribute("currentTerm");
        final FullCourseOffering course = dao.getCourseOffering(courseCode, termCode);
        model.addAttribute("course", course);
        
        final CourseSection section = dao.getCourseSectionOffering(courseCode, sectionCode, termCode);
        model.addAttribute("section", section);

        final School school = dao.getSchool(schoolCode);
        model.addAttribute("school", school);
        
        final Department department = dao.getDepartment(schoolCode, departmentCode, termCode);
        model.addAttribute("department", department);
        
        Map<String, String> instructorUrls = new HashMap<String, String>();
        for (Instructor instructor : section.getInstructors()) {
            instructorUrls.put(instructor.getIdentifier(), urlService.getInstructorUrl(instructor, request));
        }
        model.put("instructorUrls", instructorUrls);
        
        Map<String, String> locationUrls = new HashMap<String, String>();
        for (final CourseMeeting meeting : section.getCourseMeetings()) {
            Location location = meeting.getLocation();
            if (location != null && !locationUrls.containsKey(location.getIdentifier())) {
                locationUrls.put(location.getIdentifier(), urlService.getLocationUrl(location, request));
            }
        }
        model.put("locationUrls", locationUrls);

        final String view = viewSelector.isMobile(request) ? "course-catalog/section-jQM" : "course-catalog/section";
        return view;
    }
    
}
