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

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.model.xml.personal.TermsAndCourses;
import org.jasig.portlet.courses.util.IParameterEvaluator;
import org.jasig.portlet.courses.util.TermCodeParameterEvaluator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;


/**
 * HttpClientCoursesDaoImpl retrieves courses from a Basic Authentication
 * protected XML feed.
 * 
 * @author Jen Bourey, jennifer.bourey@gmail.com
 * @version $Revision$
 */
public class HttpClientCoursesDaoImpl implements ICoursesDao {
    public static final String PROPERTY_KEY_TERMCODE = "#TERMCODE#";

    private final Log log = LogFactory.getLog(getClass());

    private String termsUrlFormat = null;

    public void setTermsUrlFormat(String urlFormat) {
        this.termsUrlFormat = urlFormat;
    }

    private String coursesUrlFormat = null;

    public void setCoursesUrlFormat(String urlFormat) {
        this.coursesUrlFormat = urlFormat;
    }

    private Map<String,IParameterEvaluator> urlParams = new HashMap<String,IParameterEvaluator>();

    public void setUrlParams(Map<String,IParameterEvaluator> params) {
        this.urlParams = params;
    }

    private IParameterEvaluator usernameEvaluator;
    
    public void setUsernameEvaluator(IParameterEvaluator usernameEvaluator) {
        this.usernameEvaluator = usernameEvaluator;
    }
    
    private IParameterEvaluator passwordEvaluator;
    
    public void setPasswordEvaluator(IParameterEvaluator passwordEvaluator) {
        this.passwordEvaluator = passwordEvaluator;
    }
    
    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TermList getTermList(PortletRequest request) {
        Map<String,String> params = createParameters(request, urlParams);
        return getTermsAndCourses(request,termsUrlFormat,params).getTermList();
    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode) {
        setTermCodeRequestAttribute(request,termCode);
        Map<String,String> params = createParameters(request,urlParams);
        return getTermsAndCourses(request,coursesUrlFormat,params).getCoursesByTerm(termCode);
    }
    
    /**
     * Get terms and courses for the current user
     */
    protected TermsAndCourses getTermsAndCourses(PortletRequest request, String uriTemplate, Map<String,String> params) {

        if (log.isDebugEnabled()) {
            log.debug("Invoking uri '" + uriTemplate 
                    + "' with the following parameters:  " 
                    + params.toString());
        }

        HttpEntity<?> requestEntity = getRequestEntity(request);
        HttpEntity<TermsAndCourses> response = restTemplate.exchange(
                uriTemplate, HttpMethod.GET, requestEntity,
                TermsAndCourses.class, params);

        return response.getBody();

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

    protected void setTermCodeRequestAttribute(PortletRequest request,String termCode) {
        for(IParameterEvaluator evaluator : urlParams.values()) {
            if(evaluator instanceof TermCodeParameterEvaluator) {
                request.setAttribute(((TermCodeParameterEvaluator)evaluator).getAttributeKey(),termCode);
                return;
            }
        }
    }

    protected Map<String,String> createParameters(PortletRequest request,Map<String,IParameterEvaluator> params) {
        Map<String,String> result = new HashMap<String,String>();

        for(String key : params.keySet()) {
            result.put(key,params.get(key).evaluate(request));
        }
        return result;
    }
}
