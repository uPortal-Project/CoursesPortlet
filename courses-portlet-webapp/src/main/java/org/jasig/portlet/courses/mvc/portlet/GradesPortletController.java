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

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.CourseSummary;
import org.jasig.portlet.courses.mvc.IViewSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class GradesPortletController {

    private ICoursesDao coursesDao;

    @Autowired(required = true)
    public void setCoursesDao(ICoursesDao coursesDao) {
        this.coursesDao = coursesDao;
    }
    
    private IViewSelector viewSelector;
    
    @Autowired(required = true)
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }
    
    @RequestMapping
    public ModelAndView getGrades(PortletRequest request, @RequestParam(required=false) String termCode) {
        
        // get the course summary for the current user        
        CourseSummary summary = coursesDao.getSummary(request);
        
        // add the user's overall GPA, credit count, and a list of terms to the
        // model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("gpa", summary.getGpa());
        model.put("credits", summary.getCredits());
        model.put("terms", summary.getTerms());

        // add the selected or current term to the model
        if (termCode == null){
            model.put("selectedTerm", summary.getCurrentTerm());
        } else {
            model.put("selectedTerm", summary.getTerm(termCode));
        }
        
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "grades-jQM" : "grades";
        
        return new ModelAndView(viewName, model);
    }
    
}
