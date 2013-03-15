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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.dao.ICacheableCoursesDao;
import org.jasig.portlet.courses.dao.xml.HttpClientCoursesDaoImpl.CoursesCacheKey;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.model.xml.personal.TermsAndCourses;
import org.jasig.portlet.courses.util.IParameterEvaluator;
import org.jasig.portlet.courses.util.TermCodeParameterEvaluator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;


/**
 * HttpClientCoursesDaoImpl retrieves courses from a Basic Authentication
 * protected XML feed.
 * 
 * @author Jen Bourey, jennifer.bourey@gmail.com
 * @version $Revision$
 */
public class HttpClientCoursesDaoImpl implements ICacheableCoursesDao<CoursesCacheKey, CoursesCacheKey> {
    public static final String PROPERTY_KEY_TERMCODE = "#TERMCODE#";

    private final Log log = LogFactory.getLog(getClass());

    private String termsUrlFormat = null;
    private String coursesUrlFormat = null;
    private Map<String,IParameterEvaluator> urlParamEvaluators = new HashMap<String,IParameterEvaluator>();
    private IParameterEvaluator usernameEvaluator;
    private IParameterEvaluator passwordEvaluator;
    private RestOperations restTemplate;

    public void setTermsUrlFormat(String urlFormat) {
        this.termsUrlFormat = urlFormat;
    }

    public void setCoursesUrlFormat(String urlFormat) {
        this.coursesUrlFormat = urlFormat;
    }

    public void setUrlParams(Map<String,IParameterEvaluator> params) {
        this.urlParamEvaluators = params;
    }
    
    public void setUsernameEvaluator(IParameterEvaluator usernameEvaluator) {
        this.usernameEvaluator = usernameEvaluator;
    }
    
    public void setPasswordEvaluator(IParameterEvaluator passwordEvaluator) {
        this.passwordEvaluator = passwordEvaluator;
    }

    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CoursesCacheKey getTermListKey(PortletRequest request) {
        final String username = usernameEvaluator.evaluate(request);
        final String password = passwordEvaluator.evaluate(request);
        final Map<String, String> params = createParameters(request, urlParamEvaluators);
        return new CoursesCacheKey(username, password.toCharArray(), params);
    }

    @Override
    public TermList getTermList(CoursesCacheKey key) {
        final TermsAndCourses termsAndCourses = getTermsAndCourses(key, termsUrlFormat);
        return termsAndCourses.getTermList();
    }
    
    @Override
    public CoursesCacheKey getCoursesByTermKey(PortletRequest request, String termCode) {
        final String username = usernameEvaluator.evaluate(request);
        final String password = passwordEvaluator.evaluate(request);
        final Map<String, String> params = createParameters(request, urlParamEvaluators);
        return new CoursesCacheKey(username, password.toCharArray(), params, termCode);
    }

    @Override
    public CoursesByTerm getCoursesByTerm(CoursesCacheKey key) {
        final TermsAndCourses termsAndCourses = getTermsAndCourses(key, coursesUrlFormat);
        return termsAndCourses.getCoursesByTerm(key.getTermCode());
    }

    protected TermsAndCourses getTermsAndCourses(CoursesCacheKey key, String url) {
        Map<String,String> params = key.getParams();

        if (log.isDebugEnabled()) {
            log.debug("Invoking uri '" + url 
                    + "' with the following parameters:  " 
                    + params.toString());
        }

        HttpEntity<?> requestEntity = getRequestEntity(key);
        HttpEntity<TermsAndCourses> response = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity,
                TermsAndCourses.class, params);

        final TermsAndCourses termsAndCourses = response.getBody();
        return termsAndCourses;
    }

    /**
     * Get a request entity prepared for basic authentication.
     */
    protected HttpEntity<?> getRequestEntity(CoursesCacheKey credentials) {
        final String username = credentials.getUsername();
        final char[] password = credentials.getPassword();
        
        if (log.isDebugEnabled()) {
            boolean hasPassword = password != null;
            log.debug("Preparing HttpEntity for user '" + username + "' (password provided = " +  hasPassword + ")");
        }
        
        HttpHeaders requestHeaders = new HttpHeaders();
        String authString = username.concat(":").concat(new String(password));
        String encodedAuthString = new Base64().encodeToString(authString.getBytes());
        requestHeaders.set("Authorization", "Basic ".concat(encodedAuthString));

        HttpEntity<?> rslt = new HttpEntity<Object>(requestHeaders);
        return rslt;

    }

    protected void setTermCodeRequestAttribute(PortletRequest request, String termCode) {
        for(IParameterEvaluator evaluator : urlParamEvaluators.values()) {
            if(evaluator instanceof TermCodeParameterEvaluator) {
                request.setAttribute(((TermCodeParameterEvaluator)evaluator).getAttributeKey(),termCode);
                return;
            }
        }
    }

    protected Map<String,String> createParameters(PortletRequest request, Map<String,IParameterEvaluator> params) {
        Map<String,String> result = new HashMap<String,String>();

        for(String key : params.keySet()) {
            result.put(key,params.get(key).evaluate(request));
        }
        return result;
    }
    
    public static final class CoursesCacheKey implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private final SecureRequestCredentials credentials;
        private final Map<String, String> params;
        private final String termCode;
        
        private CoursesCacheKey(String username, char[] password, Map<String, String> params) {
            this(username, password, params, null);
        }
        
        private CoursesCacheKey(String username, char[] password, Map<String, String> params, String termCode) {
            this.credentials = new SecureRequestCredentials(username, password);
            this.params = params;
            this.termCode = termCode;
        }

        private String getUsername() {
            return credentials.getUsername();
        }

        private char[] getPassword() {
            return credentials.getPassword();
        }

        private Map<String, String> getParams() {
            return params;
        }

        public String getTermCode() {
            return termCode;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((credentials == null) ? 0 : credentials.hashCode());
            result = prime * result + ((params == null) ? 0 : params.hashCode());
            return result;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CoursesCacheKey other = (CoursesCacheKey) obj;
            if (credentials == null) {
                if (other.credentials != null)
                    return false;
            }
            else if (!credentials.equals(other.credentials))
                return false;
            if (params == null) {
                if (other.params != null)
                    return false;
            }
            else if (!params.equals(other.params))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "CoursesCacheKey [credentials=" + credentials + ", params=" + params + "]";
        }
    }
}
