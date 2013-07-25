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

package org.jasig.portlet.degreeprogress.dao.xml;

import java.util.HashMap;
import java.util.Map;
import javax.portlet.PortletRequest;
import javax.xml.bind.JAXBElement;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.util.IParameterEvaluator;
import org.jasig.portlet.degreeprogress.dao.IDegreeProgressDao;
import org.jasig.portlet.degreeprogress.dao.WhatIfRequest;
import org.jasig.portlet.degreeprogress.model.StudentCourseRegistration;
import org.jasig.portlet.degreeprogress.model.xml.Course;
import org.jasig.portlet.degreeprogress.model.xml.CourseRequirement;
import org.jasig.portlet.degreeprogress.model.xml.DegreeProgressReport;
import org.jasig.portlet.degreeprogress.model.xml.DegreeRequirementSection;
import org.jasig.portlet.degreeprogress.model.xml.GeneralRequirementType;
import org.jasig.portlet.degreeprogress.model.xml.GpaRequirement;
import org.jasig.portlet.degreeprogress.model.xml.Grade;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * HttpDegreeProgressDaoImpl retrieves degree progress from a Basic Authentication
 * protected XML feed.
 *
 * @author Chris Waymire, chris@waymire.net
 */
public class HttpDegreeProgressDaoImpl implements IDegreeProgressDao {
    private final Log log = LogFactory.getLog(getClass());

    private IParameterEvaluator usernameEvaluator;

    public void setUsernameEvaluator(IParameterEvaluator usernameEvaluator) {
        this.usernameEvaluator = usernameEvaluator;
    }

    private IParameterEvaluator passwordEvaluator;

    private Map<String,IParameterEvaluator> urlParams = new HashMap<String,IParameterEvaluator>();

    public void setUrlParams(Map<String,IParameterEvaluator> params) {
        this.urlParams = params;
    }

    public void setPasswordEvaluator(IParameterEvaluator passwordEvaluator) {
        this.passwordEvaluator = passwordEvaluator;
    }

    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String degreeProgressUrlFormat = null;

    public void setDegreeProgressUrlFormat(String urlFormat) {
        this.degreeProgressUrlFormat = urlFormat;
    }

    @Override
    public DegreeProgressReport getProgressReport(PortletRequest request) {
        Map<String,String> params = createParameters(request, urlParams);
        if (log.isDebugEnabled()) {
            log.debug("Invoking uri '" + degreeProgressUrlFormat + "' with the following parameters:  " + params.toString());
        }

        HttpEntity<?> requestEntity = getRequestEntity(request);
        HttpEntity<DegreeProgressReport> response = restTemplate.exchange(
                degreeProgressUrlFormat, HttpMethod.GET, requestEntity,
                DegreeProgressReport.class, params);

        DegreeProgressReport report = response.getBody();
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
            report.addSection(section);
        }
        return report;
    }

    @Override
    public Boolean getWebEnabled(PortletRequest request) {
        return Boolean.TRUE;
    }

    @Override
    public WhatIfRequest createWhatIfRequest(PortletRequest request) {
        return null;
    }

    @Override
    public DegreeProgressReport getWhatIfReport(WhatIfRequest whatIfRequest) {
        return null;
    }

    /**
     * Get a request entity prepared for basic authentication.
     */
    protected HttpEntity<?> getRequestEntity(PortletRequest request) {

        String username = usernameEvaluator.evaluate(request);
        String password = passwordEvaluator.evaluate(request);

        if (log.isDebugEnabled()) {
            boolean hasPassword = password != null;
            log.debug("Preparing HttpEntity for user '" + username + "' (password provided = " +  hasPassword + ")");
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        String authString = username.concat(":").concat(password);
        String encodedAuthString = new Base64().encodeToString(authString.getBytes());
        requestHeaders.set("Authorization", "Basic ".concat(encodedAuthString));

        HttpEntity<?> rslt = new HttpEntity<Object>(requestHeaders);
        return rslt;

    }

    protected Map<String,String> createParameters(PortletRequest request,Map<String,IParameterEvaluator> params) {
        Map<String,String> result = new HashMap<String,String>();

        for(String key : params.keySet()) {
            result.put(key,params.get(key).evaluate(request));
        }
        return result;
    }
}
