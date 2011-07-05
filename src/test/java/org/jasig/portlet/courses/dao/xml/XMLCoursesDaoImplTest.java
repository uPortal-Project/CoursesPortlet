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

import static org.junit.Assert.assertEquals;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.wrapper.CourseSummaryWrapper;
import org.jasig.portlet.courses.model.xml.Course;
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
        CourseSummaryWrapper summary = dao.getSummary(request);
        assertEquals(1, summary.getNewAnnouncementCount());
        assertEquals(5, summary.getCourses().size());
        assertEquals(3.25, summary.getGpa().doubleValue(), .01);
        
        Course course1 = summary.getCourses().get(0);
        assertEquals("Design Awareness", course1.getTitle());
    }

}
