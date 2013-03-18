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

import java.io.IOException;
import java.io.Serializable;

import javax.portlet.PortletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.dao.ICacheableCoursesDao;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.model.xml.personal.TermsAndCourses;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class MockCoursesDaoImpl implements ICacheableCoursesDao<Serializable, String>, InitializingBean {
    private static final Serializable TERM_LIST_KEY = TermListKey.INSTANCE;
    public enum TermListKey {
        INSTANCE
    }
    
    protected final Log log = LogFactory.getLog(getClass());
    
    private TermsAndCourses summary;
    private Resource mockData;
    
    public void setMockData(Resource mockData) {
        this.mockData = mockData;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());
        } catch (IOException e) {
            log.error("Failed to read mock data", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data", e);
        }
    }
    
    @Override
    public Serializable getTermListKey(PortletRequest request) {
        return TERM_LIST_KEY;
    }

    @Override
    public String getCoursesByTermKey(PortletRequest request, String termCode) {
        return termCode;
    }

    @Override
    public TermList getTermList(Serializable key) {
        return this.summary.getTermList();
    }

    @Override
    public CoursesByTerm getCoursesByTerm(String termCode) {
        return this.summary.getCoursesByTerm(termCode);
    }
}
