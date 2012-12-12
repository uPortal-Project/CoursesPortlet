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
import java.util.Properties;

import javax.portlet.PortletRequest;

import org.apache.commons.codec.binary.Base64;
import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.model.xml.personal.TermsAndCourses;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String termsUrlFormat = null;

    public void setTermsUrlFormat(String urlFormat) {
        this.termsUrlFormat = urlFormat;
    }

    private Map<String,String> termsUrlParams = new HashMap<String,String>();

    public void setTermsUrlParams(Map<String,String> params) {
        this.termsUrlParams = params;
    }

    private String coursesUrlFormat = null;

    public void setCoursesUrlFormat(String urlFormat) {
        this.coursesUrlFormat = urlFormat;
    }

    private Map<String,String> coursesUrlParams = new HashMap<String,String>();

    public void setCoursesUrlParams(Map<String,String> params) {
        this.coursesUrlParams = params;
    }

    private String usernameKey = "user.login.id";
    
    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }
    
    private String passwordKey = "password";
    
    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }
    
    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TermList getTermList(PortletRequest request) {
        Map<String,String> params = createParameters(request, termsUrlParams, null);
        return getTermsAndCourses(request,termsUrlFormat,params).getTermList();
    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode) {
        Properties props =  new Properties();
        props.setProperty(PROPERTY_KEY_TERMCODE,termCode);
        Map<String,String> params = createParameters(request,coursesUrlParams,props);
        return getTermsAndCourses(request,coursesUrlFormat,params).getCoursesByTerm(termCode);
    }
    
    /**
     * Get terms and courses for the current user
     */
    protected TermsAndCourses getTermsAndCourses(PortletRequest request,String url, Map<String,String> params) {
        HttpEntity<?> requestEntity = getRequestEntity(request);
        HttpEntity<TermsAndCourses> response = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                TermsAndCourses.class,params);

        return response.getBody();
    }

    /**
     * Get a request entity prepared for basic authentication.
     */
    protected HttpEntity<?> getRequestEntity(PortletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String,String> userInfo = (Map<String,String>) request.getAttribute(PortletRequest.USER_INFO);
        String username = userInfo.get(this.usernameKey);
        String password = userInfo.get(this.passwordKey);

        HttpHeaders requestHeaders = new HttpHeaders();
        String authString = username.concat(":").concat(password);
        String encodedAuthString = new Base64().encodeToString(authString.getBytes());
        requestHeaders.set("Authorization", "Basic ".concat(encodedAuthString));
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
        return requestEntity;
    }

    protected Map<String,String> createParameters(PortletRequest request,Map<String,String> params,Properties props) {
        Map<String,String> userInfo = (Map<String,String>) request.getAttribute(PortletRequest.USER_INFO);
        Map<String,String> result = new HashMap<String,String>();
        if(props == null) props = new Properties();

        for(String key : params.keySet()) {
            if(PROPERTY_KEY_TERMCODE.equals(params.get(key))) {
                result.put(key,props.getProperty(PROPERTY_KEY_TERMCODE));
                continue;
            }
            result.put(key,userInfo.get(params.get(key)));
        }
        return result;
    }
}
