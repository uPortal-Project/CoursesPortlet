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
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.wrapper.CourseSummaryWrapper;
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
public class MergingCoursesDaoImplTest {
    
    @Autowired
    MergingCoursesDaoImpl dao;

    @Mock PortletRequest request;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Map<String, String> userInfo = new HashMap<String,String>();
        userInfo.put("user.login.id", "student");
        userInfo.put("password", "student");
        when(request.getAttribute(PortletRequest.USER_INFO)).thenReturn(userInfo);
        when(request.getRemoteUser()).thenReturn("student");

    }

    @Test
    public void test() {
        CourseSummaryWrapper summary = dao.getSummary(request);
        assertEquals(6, summary.getCourses().size());        
    }

}
