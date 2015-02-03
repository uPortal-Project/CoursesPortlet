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
package org.jasig.portlet.degreeprogress.dao.mock;

import java.io.IOException;
import javax.portlet.PortletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.degreeprogress.dao.IDegreeProgressDao;
import org.jasig.portlet.degreeprogress.model.xml.CourseRequirement;
import org.jasig.portlet.degreeprogress.model.xml.DegreeProgressReport;
import org.jasig.portlet.degreeprogress.model.xml.DegreeRequirementSection;
import org.jasig.portlet.degreeprogress.model.xml.GeneralRequirementType;
import org.jasig.portlet.degreeprogress.dao.WhatIfRequest;
import org.jasig.portlet.degreeprogress.model.xml.Course;
import org.jasig.portlet.degreeprogress.model.xml.GpaRequirement;
import org.jasig.portlet.degreeprogress.model.xml.Grade;
import org.jasig.portlet.degreeprogress.model.StudentCourseRegistration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

public class MockDegreeProgressDaoImpl implements IDegreeProgressDao, InitializingBean {

    protected final Log log = LogFactory.getLog(getClass());

    private DegreeProgressReport report;
    
    private Resource mockData = new ClassPathResource("/mock-data/mock-progress-report.xml");

    public void setMockData(Resource mockData) {
        this.mockData = mockData;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(org.jasig.portlet.degreeprogress.model.xml.DegreeProgressReport.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.report = (DegreeProgressReport) unmarshaller.unmarshal(mockData.getInputStream());

            for (DegreeRequirementSection section : report.getDegreeRequirementSections()) {
                for (JAXBElement<? extends GeneralRequirementType> requirement : section.getGeneralRequirements()) {
                    GeneralRequirementType req = requirement.getValue();
                    if (req instanceof GpaRequirement) {
                        section.setRequiredGpa(((GpaRequirement) req).getRequiredGpa());
                    }
                }
                
                for (CourseRequirement req : section.getCourseRequirements()) {
                    for (Course course : req.getCourses()) {
                        StudentCourseRegistration registration = new StudentCourseRegistration();
                        registration.setCredits(course.getCredits());
                        registration.setSource(course.getSource());
                        registration.setSemester(course.getSemester());
                        registration.setCourse(course);

                        Grade grade = new Grade();
                        grade.setCode(course.getGrade().getCode());
                        registration.setGrade(grade);

                        req.getRegistrations().add(registration);
                    }
                }
                this.report.addSection(section);
            }
        } catch (IOException e) {
            log.error("Failed to read mock data", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data", e);
        }
    }

    public DegreeProgressReport getProgressReport(PortletRequest request) {
        return this.report;        
    }

    @Override
    public Boolean getWebEnabled(PortletRequest request) {
        // TODO Auto-generated method stub
        return Boolean.TRUE;
    }

    @Override
    public WhatIfRequest createWhatIfRequest(PortletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DegreeProgressReport getWhatIfReport(WhatIfRequest whatIfRequest) {
        // TODO Auto-generated method stub
        return null;
    }

}
