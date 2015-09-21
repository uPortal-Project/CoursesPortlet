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
package org.jasig.portlet.courses.service;

import static org.junit.Assert.assertEquals;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UPortalURLServiceImplTest {
    
    UPortalURLServiceImpl urlService;
    
    @Mock
    PortletRequest request;
    
    @Before
    public void setUp() {
        
        MockitoAnnotations.initMocks(this);
        
        urlService = new UPortalURLServiceImpl();
        urlService.setPortalContext("/context");
    }
    
    @Test
    public void testLocationUrl() {
        Location location = new Location();
        location.setIdentifier("ABC");
        String url = urlService.getLocationUrl(location, request);
        assertEquals("/context/s/location?id=ABC", url);
    }

    @Test
    public void testInstructorUrl() {
        Instructor instructor = new Instructor();
        instructor.setIdentifier("username");
        String url = urlService.getInstructorUrl(instructor, request);
        assertEquals("/context/s/person?id=username", url);
    }

}
