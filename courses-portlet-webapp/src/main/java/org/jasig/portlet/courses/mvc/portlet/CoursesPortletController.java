/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.courses.mvc.portlet;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.MimeResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;

import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.CourseMeeting;
import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.Course;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.service.IURLService;
import org.jasig.portlet.utils.mvc.IViewSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class CoursesPortletController {
    public static final String DEFAULT_VIEW_PREFERENCE = "defaultView";
    public static final String COURSE_LIST_VIEW = "courseList";
    public static final String GRADES_LIST_VIEW = "grades";
    public static final String TERMCODE = "termCode";
    public static final String COURSECODE = "courseCode";
    
    private static final String DISPLAY_COURSE_UPDATES_PREFERENCE = "displayCourseUpdates";
    private static final String DISPLAY_COURSE_BOOKS_PREFERENCE = "displayCourseBooks";

    private ICoursesDao coursesDao;

    @Autowired
    @Qualifier("finalGradesServiceDao")
    public void setCoursesDao(ICoursesDao coursesDao) {
        this.coursesDao = coursesDao;
    }
    
    private IURLService urlService;
    
    @Autowired 
    public void setUrlService(IURLService urlService) {
        this.urlService = urlService;
    }
    
    private IViewSelector viewSelector;
    
    @Autowired
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }
    
    @RequestMapping
    public String defaultView(PortletRequest request, ModelMap model) {
        final PortletPreferences preferences = request.getPreferences();
        final String defaultView = preferences.getValue(DEFAULT_VIEW_PREFERENCE, COURSE_LIST_VIEW);
        if (GRADES_LIST_VIEW.equals(defaultView)) {
            return viewGrades(request, model, null);
        }
        else {
            return viewCourseList(request, model, null);
        }
    }
    
    @RequestMapping(params = "action=" + COURSE_LIST_VIEW)
    public String viewCourseList(PortletRequest request, ModelMap model, 
            @RequestParam(value = TERMCODE, required = false) String termCode) {
        
        final TermList termList = coursesDao.getTermList(request);
        model.put("termList", termList);
        
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
        
        //Grab the coursesByTerm information if a selectedTerm is set
        if (selectedTerm != null) {
            final CoursesByTerm coursesByTerm = coursesDao.getCoursesByTerm(request, selectedTerm.getCode());
            model.put("coursesByTerm", coursesByTerm);
            model.put("selectedTerm", selectedTerm);
        }
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "mycourses/courseList-jQM" : "mycourses/courseList";
        return viewName;
    }

    @RequestMapping(params = "action=" + GRADES_LIST_VIEW)
    public String viewGrades(PortletRequest request, ModelMap model, 
            @RequestParam(value = TERMCODE, required = false) String termCode) {
        
        final TermList termList = coursesDao.getTermList(request);
        model.put("termList", termList);
        
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
        
        //Grab the coursesByTerm information if a selectedTerm is set
        if (selectedTerm != null) {
            final CoursesByTerm coursesByTerm = coursesDao.getCoursesByTerm(request, selectedTerm.getCode());
            model.put("coursesByTerm", coursesByTerm);
            model.put("selectedTerm", selectedTerm);
        }
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "mycourses/grades-jQM" : "mycourses/grades";
        return viewName;
    }

    @RequestMapping(params = "action=showCourse")
    public String viewCourse(PortletRequest request, ModelMap model,
            @RequestParam(TERMCODE) String termCode, 
            @RequestParam(COURSECODE) String courseCode) {
        
        CoursesByTerm summary = coursesDao.getCoursesByTerm(request, termCode);
        model.put("coursesByTerm", summary);
        
        Course selectedCourse = summary.getCourse(courseCode);
        
        Map<String, String> instructorUrls = new HashMap<String, String>();
        for (Instructor instructor : selectedCourse.getInstructors()) {
            instructorUrls.put(instructor.getIdentifier(), urlService.getInstructorUrl(instructor, request));
        }
        model.put("instructorUrls", instructorUrls);

        Map<String, String> locationUrls = new HashMap<String, String>();
        for (final CourseMeeting meeting : selectedCourse.getCourseMeetings()) {
            Location location = meeting.getLocation();
            if (location != null && !locationUrls.containsKey(location.getIdentifier())) {
                locationUrls.put(location.getIdentifier(), urlService.getLocationUrl(location, request));
            }
        }
        model.put("locationUrls", locationUrls);
        
        model.put("course", selectedCourse);
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "mycourses/courseDetail-jQM" : "mycourses/courseDetail";
        return viewName;
    }
    
    @ResourceMapping("gradesUpdate")
    public String getGradesFragment(ResourceRequest resourceRequest, ModelMap model,
            @RequestParam(TERMCODE) String termCode) {
        
        final TermList termList = coursesDao.getTermList(resourceRequest);
        model.put("termList", termList);

        //Determine the current term code and term
        termCode = this.getSelectedTermCode(resourceRequest, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
        if (selectedTerm == null) {
            throw new IllegalArgumentException("Could not find term for code: " + termCode); 
        }
        
        //Grab the coursesByTerm information if a selectedTerm is set
        final CoursesByTerm coursesByTerm = coursesDao.getCoursesByTerm(resourceRequest, selectedTerm.getCode());
        model.put("coursesByTerm", coursesByTerm);
        model.put("selectedTerm", selectedTerm);
        
        //TODO does this need a mobile specific view? If so uncomment these lines and 
//        final boolean isMobile = viewSelector.isMobile(resourceRequest);
//        final String viewName = isMobile ? "fragments/gradesUpdate-jQM" : "fragments/gradesUpdate";
        final String viewName = "fragments/gradesUpdate";
        return viewName;
    }
    
    /**
     * Action request handler that simply copies all parameters from action to render
     */
    @ActionMapping
    public void copyActionParameters(ActionRequest actionRequest, ActionResponse actionResponse) {
        actionResponse.setRenderParameters(actionRequest.getParameterMap());
    }
    
    @ModelAttribute("displayCourseUpdates")
    public Boolean getDisplayCourseUpdates(PortletRequest req) {
        String val = req.getPreferences().getValue(DISPLAY_COURSE_UPDATES_PREFERENCE, 
                Boolean.TRUE.toString());
        return Boolean.valueOf(val);
    }
    
    @ModelAttribute("displayCourseBooks")
    public Boolean getDisplayCourseBooks(PortletRequest req) {
        String val = req.getPreferences().getValue(DISPLAY_COURSE_BOOKS_PREFERENCE, 
                Boolean.TRUE.toString());
        return Boolean.valueOf(val);
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
     * If termCode is null {@link TermList#getCurrentTerm()} is used, if not {@link TermList#getTerm(String)} is used
     */
    protected Term getSelectedTerm(String termCode, final TermList termList) {
        if (termCode == null) {
            return termList.getCurrentTerm();
        }

        return termList.getTerm(termCode);
    }
    
    @ResourceMapping("jsonCurrentClassSchedule")
    public String jsonCurrentClassSchedule(PortletRequest request, MimeResponse response, ModelMap model)
    {
      final TermList termList = coursesDao.getTermList(request);
      if(termList != null) {
        model.put("termList", termList);
        if(termList.getCurrentTerm() != null) {
          final CoursesByTerm currentTermCourses = coursesDao.getCoursesByTerm(request, termList.getCurrentTerm().getCode());
          model.put("currentTermCourses", currentTermCourses);
        } else {
          model.put("currentTermCourses", null);
        }
      }
      
      return "json";
      
    }
}