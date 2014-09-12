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
package org.jasig.portlet.grades.mvc;
import java.util.HashMap;
import java.util.Map;
import javax.portlet.*;

import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.utils.mvc.IViewSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @RenderMapping
    public ModelAndView getGrades(PortletRequest request, @RequestParam(required=false) String termCode) {

      final Map<String, Object> model = new HashMap<String, Object>();
        PortletPreferences prefs = request.getPreferences();
        request.getPortletSession().setAttribute("helpDeskURL",prefs.getValue("helpDeskURL", "https://kb.wisc.edu/helpdesk/page.php?id=24398"));
        final TermList termList = coursesDao.getTermList(request);
        model.put("termList", termList);
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
        //Grab the courseSummary information if a selectedTerm is set
        if (selectedTerm != null) {
            final CoursesByTerm courseSummary = coursesDao.getCoursesByTerm(request, selectedTerm.getCode());
            model.put("courseSummary", courseSummary);
            model.put("selectedTerm", selectedTerm);
        }
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "final-grades/grades-jQM" : "final-grades/grades";
        return new ModelAndView(viewName, model);
    }
    @ResourceMapping("gradesUpdate")
    public ModelAndView getCoursesSummary(ResourceRequest resourceRequest, @RequestParam String termCode) {
        final Map<String, Object> model = new HashMap<String, Object>();
        final TermList termList = coursesDao.getTermList(resourceRequest);
        model.put("termList", termList);
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(resourceRequest, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
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
        final String viewName = "final-grades/fragments/gradesUpdate";
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
     * If termCode is null {@link termList#getCurrentTerm()} is used, if not {@link termList#getTerm(String)} is used
     */
    protected Term getSelectedTerm(String termCode, final TermList termList) {
        if (termCode == null) {
            return termList.getCurrentTerm();
        }
        return termList.getTerm(termCode);
    }
}