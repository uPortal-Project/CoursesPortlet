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
package org.jasig.portlet.courses.dao.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.Course;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.model.xml.personal.TermsAndCourses;
import org.springframework.core.io.Resource;

import javax.portlet.PortletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: saikatsengupta
 * Date: 10/15/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockGradesDaoImpl implements ICoursesDao {

    protected final Log log = LogFactory.getLog(getClass());
    private TermsAndCourses summary;
    private Resource mockData;

    public void setMockData(Resource mockData) {
        this.mockData = mockData;
    }


    @Override
    public TermList getTermList(PortletRequest request) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());
        } catch (IOException e) {
            log.error("Failed to read mock data for TermList", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data for TermList", e);
        }
        return summary.getTermList();
    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());


        } catch (IOException e) {
            log.error("Failed to read mock data for CourseByTerm", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data for CourseByTerm", e);
        }
        return this.summary.getCoursesByTerm(termCode);
    }


}
