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

package org.jasig.portlet.degreeprogress.model.xml;

import java.util.ArrayList;
import java.util.List;
import org.jasig.portlet.degreeprogress.model.StudentCourseRegistration;

public abstract class CourseRequirementWrapper {
    protected List<StudentCourseRegistration> registrations = new ArrayList<StudentCourseRegistration>();

    public List<StudentCourseRegistration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<StudentCourseRegistration> registrations) {
        this.registrations = registrations;
    }
    
    public abstract Boolean isCompleted();
    
    /**
     * Needed due to a well-known JAXB blunder:
     * 
     *  - http://stackoverflow.com/questions/4586927/using-xjcs-enableintrospection-with-jaxws-maven-plugin
     */
    public final boolean getCompleted() {
        return isCompleted();
    }
}
