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

package org.jasig.portlet.courses.dao.sakai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.sakai.xml.Refs;
import org.jasig.portlet.courses.model.wrapper.CourseSummaryWrapper;
import org.jasig.portlet.courses.model.xml.Course;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/testContext.xml")
@Ignore
public class SakaiCoursesDaoImplTest {

    SakaiCoursesDaoImpl dao;
    @Autowired RestTemplate restTemplate;
    @Autowired ApplicationContext applicationContext;
    
    @Mock PortletRequest request;
    
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dao = spy(new SakaiCoursesDaoImpl());
        dao.setRestTemplate(restTemplate);
        
        Refs refs = restTemplate.getForObject("http://localhost/~athena/local-hosting/sakai-sites.xml", Refs.class, Collections.EMPTY_MAP);
        doReturn(refs).when(dao).getSiteData(request);
    }
    
    @Test
    public void test() {
        CourseSummaryWrapper summary = dao.getSummary(request);
        assertEquals(1, summary.getCourses().size());
        
        Course course1 = summary.getCourses().get(0);
        assertEquals("Test Class", course1.getTitle());
    }

    @Test
    public void acquireSessionTest() {
        
        Map<String, String> userInfo = new HashMap<String,String>();
        userInfo.put("user.login.id", "student");
        userInfo.put("password", "student");
        when(request.getAttribute(PortletRequest.USER_INFO)).thenReturn(userInfo);
        when(request.getRemoteUser()).thenReturn("student");
        
        String sessionId = dao.getAuthenticatedSession(request);
        assertNotNull(sessionId);
    }
    
}
