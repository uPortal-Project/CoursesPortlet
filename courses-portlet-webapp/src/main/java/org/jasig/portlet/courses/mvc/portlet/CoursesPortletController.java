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

package org.jasig.portlet.courses.mvc.portlet;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;

import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.Course;
import org.jasig.portlet.courses.model.xml.CourseSummary;
import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermSummary;
import org.jasig.portlet.courses.mvc.IViewSelector;
import org.jasig.portlet.courses.service.IURLService;
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
public class CoursesPortletController {

    private static final String TERMCODE = "termCode";
    private ICoursesDao coursesDao;

    @Autowired(required = true)
    public void setCoursesDao(ICoursesDao coursesDao) {
        this.coursesDao = coursesDao;
    }
    
    private IURLService urlService;
    
    @Autowired(required = true) 
    public void setUrlService(IURLService urlService) {
        this.urlService = urlService;
    }
    
    private IViewSelector viewSelector;
    
    @Autowired(required = true)
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }
    
    @RequestMapping
    public ModelAndView getCourseList(PortletRequest request, @RequestParam(required=false) String termCode) {
        final Map<String, Object> model = new HashMap<String, Object>();
        
        final TermSummary termSummary = coursesDao.getTermSummary(request);
        model.put("termSummary", termSummary);
        
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termSummary);
        
        //Grab the courseSummary information if a selectedTerm is set
        if (selectedTerm != null) {
            final CourseSummary courseSummary = coursesDao.getCourseSummary(request, selectedTerm.getCode());
            model.put("courseSummary", courseSummary);
            model.put("selectedTerm", selectedTerm);
        }
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "courseList-jQM" : "courseList";
        
        return new ModelAndView(viewName, model);
    }

    @RequestMapping(params = "action=grades")
    public ModelAndView getGrades(PortletRequest request, @RequestParam(required=false) String termCode) {
        final Map<String, Object> model = new HashMap<String, Object>();
        
        final TermSummary termSummary = coursesDao.getTermSummary(request);
        model.put("termSummary", termSummary);
        
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termSummary);
        
        //Grab the courseSummary information if a selectedTerm is set
        if (selectedTerm != null) {
            final CourseSummary courseSummary = coursesDao.getCourseSummary(request, selectedTerm.getCode());
            model.put("courseSummary", courseSummary);
            model.put("selectedTerm", selectedTerm);
        }
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "grades-jQM" : "grades";
        
        return new ModelAndView(viewName, model);
    }

    @RequestMapping(params = "action=showCourse")
    public ModelAndView getCourseView(@RequestParam String termCode, @RequestParam String courseCode, PortletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        
        // TODO: write a better implementation for locating an individual course
        CourseSummary summary = coursesDao.getCourseSummary(request, termCode);
        model.put("courseSummary", summary);
        
        Course selectedCourse = summary.getCourse(courseCode);
        
        Map<String, String> instructorUrls = new HashMap<String, String>();
        for (Instructor instructor : selectedCourse.getInstructors()) {
            instructorUrls.put(instructor.getIdentifier(), urlService.getInstructorUrl(instructor, request));
        }
        model.put("instructorUrls", instructorUrls);
        
        Location location = selectedCourse.getLocation();
        if (location != null) {
            model.put("locationUrl", urlService.getLocationUrl(location, request));
        }
        
        model.put("course", selectedCourse);
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "courseDetail-jQM" : "courseDetail";
        
        return new ModelAndView(viewName, model);
    }
    
    @ResourceMapping("gradesUpdate")
    public ModelAndView getCoursesSummary(ResourceRequest resourceRequest, @RequestParam String termCode) {
        final Map<String, Object> model = new HashMap<String, Object>();
        
        final TermSummary termSummary = coursesDao.getTermSummary(resourceRequest);
        model.put("termSummary", termSummary);

        //Determine the current term code and term
        termCode = this.getSelectedTermCode(resourceRequest, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termSummary);
        if (selectedTerm == null) {
            throw new IllegalArgumentException("Could not find term for code: " + termCode); 
        }
        
        //Grab the courseSummary information if a selectedTerm is set
        final CourseSummary courseSummary = coursesDao.getCourseSummary(resourceRequest, selectedTerm.getCode());
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
    protected Term getSelectedTerm(String termCode, final TermSummary termSummary) {
        if (termCode == null) {
            return termSummary.getCurrentTerm();
        }

        return termSummary.getTerm(termCode);
    }
}
