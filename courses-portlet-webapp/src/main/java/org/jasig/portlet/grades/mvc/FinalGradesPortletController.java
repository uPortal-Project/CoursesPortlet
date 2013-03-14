package org.jasig.portlet.grades.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;

import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.utils.mvc.IViewSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class FinalGradesPortletController {
    public static final String TERMCODE = "termCode";

    private ICoursesDao coursesDao;
    private IViewSelector viewSelector;
    
    @Autowired
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }

    @Autowired
    public void setCoursesDao(ICoursesDao coursesDao) {
        this.coursesDao = coursesDao;
    }

    @RenderMapping//(params = "action=grades") Make grades the default view for UW
    public ModelAndView getGrades(PortletRequest request, @RequestParam(required=false) String termCode) {
        final Map<String, Object> model = new HashMap<String, Object>();
        
        final TermList termSummary = coursesDao.getTermList(request);
        model.put("termSummary", termSummary);
        
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termSummary);
        
        //Grab the courseSummary information if a selectedTerm is set
        if (selectedTerm != null) {
            final CoursesByTerm courseSummary = coursesDao.getCoursesByTerm(request, selectedTerm.getCode());
            model.put("courseSummary", courseSummary);
            model.put("selectedTerm", selectedTerm);
        }
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "grades-jQM" : "grades";
        
        return new ModelAndView(viewName, model);
    }
    
    @ResourceMapping("gradesUpdate")
    public ModelAndView getCoursesSummary(ResourceRequest resourceRequest, @RequestParam String termCode) {
        final Map<String, Object> model = new HashMap<String, Object>();

        final TermList termSummary = coursesDao.getTermList(resourceRequest);
        model.put("termSummary", termSummary);

        //Determine the current term code and term
        termCode = this.getSelectedTermCode(resourceRequest, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termSummary);
        if (selectedTerm == null) {
            throw new IllegalArgumentException("Could not find term for code: " + termCode); 
        }
        
        //Grab the courseSummary information if a selectedTerm is set
        final CoursesByTerm courseSummary = coursesDao.getCoursesByTerm(resourceRequest, selectedTerm.getCode());
        model.put("courseSummary", courseSummary);
        model.put("selectedTerm", selectedTerm);
        
        //TODO does this need a mobile specific view? If so uncomment these lines and 
//        final boolean isMobile = viewSelector.isMobile(resourceRequest);
//        final String viewName = isMobile ? "fragments/gradesUpdate-jQM" : "fragments/gradesUpdate";
        final String viewName = "fragments/gradesUpdate";
        
        return new ModelAndView(viewName, model);
    }
    
    /**
     * Action request handler that simply copies all parameters from action to render
     */
    @ActionMapping
    public void copyActionParameters(ActionRequest actionRequest, ActionResponse actionResponse) {
        actionResponse.setRenderParameters(actionRequest.getParameterMap());
    }
    
    /**
     * Determine the term code to use. If a term code is specified on the request it is returned and stored
     * in the portlet session. If no term code is specified on the request the session is checked for a stored
     * term code.
     */
    protected String getSelectedTermCode(PortletRequest portletRequest, String requestTermCode) {
        final PortletSession portletSession = portletRequest.getPortletSession();
        if (requestTermCode != null) {
            portletSession.setAttribute(TERMCODE, requestTermCode);
            return requestTermCode;
        }
        
        return (String)portletSession.getAttribute(TERMCODE);
    }

    /**
     * If termCode is null {@link TermSummary#getCurrentTerm()} is used, if not {@link TermSummary#getTerm(String)} is used
     */
    protected Term getSelectedTerm(String termCode, final TermList termSummary) {
        if (termCode == null) {
            return termSummary.getCurrentTerm();
        }

        return termSummary.getTerm(termCode);
    }
}
