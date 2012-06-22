package org.jasig.portlet.courses.mvc.portlet;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.jasig.portlet.courses.dao.ICourseOfferingDao;
import org.jasig.portlet.courses.model.catalog.xml.CourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.Department;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.catalog.xml.Term;
import org.jasig.portlet.courses.mvc.IViewSelector;
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

    private IViewSelector viewSelector;
    
    @Autowired(required = true)
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }

    @RequestMapping
    public String showMainView(final ModelMap model, final PortletRequest request) {
        final List<School> schools = dao.getSchools();

        // if there's only one school configured, we don't need to offer users
        // a choice and should skip directly to displaying departments
        if (schools.size() == 1) {
            return showDepartments(schools.get(0).getCode(), model, request);
        } 
        
        else {
            model.addAttribute("schools", schools);

            final String view = viewSelector.isMobile(request) ? "schools-jQM" : "schools";
            return view;
        }
        
    }
    
    @RequestMapping(params = "action=departments")
    public String showDepartments(final @RequestParam String schoolCode, final ModelMap model, final PortletRequest request) {
        
        // get a list of departments
        final List<Department> departments = dao.getDepartments(schoolCode);
        model.addAttribute("departments", departments);
        
        final School school = dao.getSchool(schoolCode);
        model.addAttribute("school", school);
        
        final String view = viewSelector.isMobile(request) ? "departments-jQM" : "departments";
        return view;
    }

    @RequestMapping(params = "action=courses")
    public String showCourses(@RequestParam(required = false) String termCode, final @RequestParam String schoolCode, final @RequestParam String departmentCode, final ModelMap model, final PortletRequest request) {

        // TODO: get the term choice list
        final List<Term> terms = dao.getTerms();
        model.addAttribute("terms", terms);

        final School school = dao.getSchool(schoolCode);
        model.addAttribute("school", school);
        
        final Department department = dao.getDepartment(schoolCode, departmentCode);
        model.addAttribute("department", department);

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
                final Term term = dao.getCurrentTerm();
                termCode = term.getCode();
                session.setAttribute("currentTerm", termCode);
            }
        }
        model.addAttribute("term", termCode);

        // get a list of course offerings for the selected term and department
        final List<CourseOffering> courses = dao.getCourseOfferings(schoolCode, departmentCode, termCode);
        model.addAttribute("courses", courses);

        final String view = viewSelector.isMobile(request) ? "courses-jQM" : "courses";
        return view;
    }

    @RequestMapping(params = "action=course")
    public String showCourse(final @RequestParam String schoolCode, final @RequestParam String departmentCode, final @RequestParam String courseCode, final ModelMap model, final PortletRequest request) {

        final PortletSession session = request.getPortletSession();
        final String termCode = (String) session.getAttribute("currentTerm");
        final CourseOffering course = dao.getCourseOffering(courseCode, termCode);
        model.addAttribute("course", course);

        final School school = dao.getSchool(schoolCode);
        model.addAttribute("school", school);
        
        final Department department = dao.getDepartment(schoolCode, departmentCode);
        model.addAttribute("department", department);

        final String view = viewSelector.isMobile(request) ? "course-jQM" : "course";
        return view;
    }
    
}
