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
package org.jasig.portlet.courses.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;
import org.springframework.beans.factory.annotation.Value;

public class UPortalURLServiceImpl implements IURLService {
    
    protected final Log log = LogFactory.getLog(getClass());
    
    @Value("${portalContext}")
    private String portalContext = "/uPortal";

    public void setPortalContext(String portalContext) {
        this.portalContext = portalContext;
    }

    /* (non-Javadoc)
     * @see org.jasig.portlet.courses.service.IURLService#getLocationUrl(org.jasig.portlet.courses.model.xml.Location, javax.portlet.PortletRequest)
     */
    @Override
    public String getLocationUrl(Location location, PortletRequest request) {
        try {
            final String encodedLocation = URLEncoder.encode(location.getIdentifier(), "UTF-8");
            return portalContext.concat("/s/location?id=").concat(encodedLocation);
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to encode location id " + location.getIdentifier());
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.jasig.portlet.courses.service.IURLService#getInstructorUrl(org.jasig.portlet.courses.model.xml.Instructor, javax.portlet.PortletRequest)
     */
    @Override
    public String getInstructorUrl(Instructor instructor, PortletRequest request) {
        try {
            final String encodedUsername = URLEncoder.encode(instructor.getIdentifier(), "UTF-8");
            return portalContext.concat("/s/person?id=").concat(encodedUsername);
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to encode location id " + instructor.getIdentifier());
            return null;
        }
    }

  @Override
  public String getOtherPortletURL(PortletPreferences prefs, String fName, String state, String fixedParamString, Map<String, String> params) {
    return null;
  }

  @Override
  public String getNativeMapUrl(PortletPreferences prefs, String baseURLDomain, String room, String displayName, String streetAddress, String longtitude, String latitude, String zoom) {
    return null;
  }

}
