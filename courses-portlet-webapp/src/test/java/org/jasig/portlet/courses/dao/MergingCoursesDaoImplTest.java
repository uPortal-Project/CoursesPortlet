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

package org.jasig.portlet.courses.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.xml.Course;
import org.jasig.portlet.courses.model.xml.CourseSummary;
import org.jasig.portlet.courses.model.xml.Term;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/testContext.xml")
public class MergingCoursesDaoImplTest {
    
    MergingCoursesDaoImpl dao;

    @Mock PortletRequest request;
    
    Course course1, course2, emptyCourse;
    
    Term term1, term2, emptyTerm;
    
    List<ICoursesDao> courseDaos;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Map<String, String> userInfo = new HashMap<String,String>();
        userInfo.put("user.login.id", "student");
        userInfo.put("password", "student");
        when(request.getAttribute(PortletRequest.USER_INFO)).thenReturn(userInfo);
        when(request.getRemoteUser()).thenReturn("student");
        
        course1 = new Course();
        course1.setCode("course1");
        course1.setCredits((double) 1);
        course1.setGrade("grade1");
        course1.setMeetingTimes("time1");
        course1.setSchool("school1");
        course1.setTitle("Course 1");
        course1.setUrl("url1");

        course2 = new Course();
        course2.setCode("course2");
        course2.setCredits((double) 2);
        course2.setGrade("grade2");
        course2.setMeetingTimes("time2");
        course2.setSchool("school2");
        course2.setTitle("Course 2");
        course2.setUrl("url2");

        emptyCourse = new Course();

        term1 = new Term();
        term1.setCode("term1");
        term1.setDisplayName("Term1");
        term1.setGpa(3.75);
        term1.setCurrent(false);
        
        term2 = new Term();
        term2.setCode("term2");
        term2.setDisplayName("Term2");
        term2.setGpa(3.35);
        term2.setCurrent(false);
        
        emptyTerm = new Term();
        
        courseDaos = new ArrayList<ICoursesDao>();
        
        dao = new MergingCoursesDaoImpl();
        dao.setCourseDaos(courseDaos);

    }
    
    @Test
    public void testMergeCourses() {
        Course course = course1;
        dao.mergeCourse(course, course2);
        
        assertEquals(2, course.getCredits(), 0.1);
        assertEquals("grade2", course.getGrade());
        assertEquals("time2", course.getMeetingTimes());
        assertEquals("school2", course.getSchool());
        assertEquals("Course 2", course.getTitle());
        assertEquals("url2", course.getUrl());
        
    }

    @Test
    public void testMergeIntoEmptyCourse() {
        Course course = emptyCourse;
        dao.mergeCourse(course, course2);
        
        assertEquals(2, course.getCredits(), 0.1);
        assertEquals("grade2", course.getGrade());
        assertEquals("time2", course.getMeetingTimes());
        assertEquals("school2", course.getSchool());
        assertEquals("Course 2", course.getTitle());
        assertEquals("url2", course.getUrl());
        
    }

    @Test
    public void testMergeEmptyCourse() {
        Course course = course1;
        dao.mergeCourse(course1, emptyCourse);
        
        assertEquals(1, course.getCredits(), 0.1);
        assertEquals("grade1", course.getGrade());
        assertEquals("time1", course.getMeetingTimes());
        assertEquals("school1", course.getSchool());
        assertEquals("Course 1", course.getTitle());
        assertEquals("url1", course.getUrl());
        
    }

    @Test
    public void testMergeSummaryData() {
        CourseSummary summary1 = new CourseSummary();
        summary1.setGpa((double) 3.3);
        summary1.setCredits((double) 40);
        summary1.getTerms().add(term1);
        
        ICoursesDao courseDao1 = mock(ICoursesDao.class); 
        when(courseDao1.getSummary(request)).thenReturn(summary1);
        courseDaos.add(courseDao1);
        
        CourseSummary summary2 = new CourseSummary();
        summary2.setGpa((double) 3.5);
        summary2.setCredits((double) 30);
        summary2.getTerms().add(term2);

        ICoursesDao courseDao2 = mock(ICoursesDao.class);
        when(courseDao2.getSummary(request)).thenReturn(summary2);
        courseDaos.add(courseDao2);

        dao.getSummary(request);
        assertEquals(3.5, summary1.getGpa(), 0.1);
        assertEquals(30, summary2.getCredits(), 0.1);
        assertEquals(2, summary1.getTerms().size());
    }

}
