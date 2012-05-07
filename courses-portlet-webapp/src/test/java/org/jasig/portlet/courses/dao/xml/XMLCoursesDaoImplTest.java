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

package org.jasig.portlet.courses.dao.xml;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.xml.Course;
import org.jasig.portlet.courses.model.xml.CourseSummary;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermSummary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/testContext.xml")
public class XMLCoursesDaoImplTest {

    @Autowired MockCoursesDaoImpl dao;
    
    @Mock PortletRequest request;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test() {
        final TermSummary termSummary = dao.getTermSummary(request);
        
        final List<Term> terms = termSummary.getTerms();
        assertEquals(2, terms.size());
        
        Term term = terms.get(0);
        assertFalse(term.isCurrent());
        assertEquals("2012w", term.getCode());
        assertEquals("Winter 2012", term.getDisplayName());
        assertNull(term.getTermType());
        assertNull(term.getYear());
        
        CourseSummary courseSummary = dao.getCourseSummary(request, term.getCode());
        assertNull(courseSummary);

        term = terms.get(1);
        assertTrue(term.isCurrent());
        assertEquals(term, termSummary.getCurrentTerm());
        assertEquals("2012s", term.getCode());
        assertEquals("Spring 2012", term.getDisplayName());
        assertNull(term.getTermType());
        assertNull(term.getYear());
        
        
        courseSummary = dao.getCourseSummary(request, terms.get(1).getCode());
        assertNotNull(courseSummary);

        assertEquals(40, courseSummary.getCredits(), 0.01);
        assertEquals(3.25, courseSummary.getGpa(), 0.01);
        assertEquals(1, courseSummary.getNewUpdateCount());
        assertEquals("2012s", courseSummary.getTermCode());
        
        final List<Course> courses = courseSummary.getCourses();
        assertEquals(5, courses.size());
        
        Course course = courses.get(0); 
        assertEquals("Design Awareness", course.getTitle());
        assertEquals("A", course.getGrade());
        assertEquals(4, course.getCredits(), 0.01);
        assertEquals(course, courseSummary.getCourse(course.getCode()));
    }

}
