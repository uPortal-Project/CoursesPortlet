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
import org.jasig.portlet.courses.model.xml.Course;
import org.jasig.portlet.courses.model.xml.CourseSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class CoursesPortletController {

    private ICoursesDao coursesDao;

    @Autowired(required = true)
    public void setCoursesDao(ICoursesDao coursesDao) {
        this.coursesDao = coursesDao;
    }
    
    @RequestMapping
    public ModelAndView getCourseList(PortletRequest request) {
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("courseList", coursesDao.getSummary(request));
        
        return new ModelAndView("courseList", model);
    }
    
    @RequestMapping(params = "action=showCourse")
    public ModelAndView getCourseView(@RequestParam String courseCode, PortletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        
        // TODO: write a better implementation for locating an individual course
        CourseSummary summary = coursesDao.getSummary(request);
        Course selectedCourse = null;
        for (Course course : summary.getCourses()) {
            if (courseCode.equals(course.getCode())) {
                selectedCourse = course;
                break;
            }
        }
        model.put("course", selectedCourse);
        
        return new ModelAndView("courseDetail", model);
    }

}
