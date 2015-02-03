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

import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import java.util.Map;

public interface IURLService {

  public String getLocationUrl(Location location, PortletRequest request);

  public String getInstructorUrl(Instructor instructor, PortletRequest request);

  public String getOtherPortletURL(PortletPreferences prefs,
                                   String fName,
                                   String state,
                                   String fixedParamString,
                                   Map<String, String> params);

  public String getNativeMapUrl(PortletPreferences prefs,
                                String baseURLDomain,
                                String room,
                                String displayName,
                                String streetAddress,
                                String longtitude,
                                String latitude,
                                String zoom);

}