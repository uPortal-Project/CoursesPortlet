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
package org.jasig.portlet.courses.model.xml;

import java.util.List;


/**
 * Adds base functionality to the {@link CourseSummary} object
 * 
 * @author Eric Dalquist
 */
public abstract class CourseSummaryWrapper {
    
    public int getNewUpdateCount() {
        int newCount = 0;
        for (Course course : this.getCourses()) {
            newCount += course.getNewUpdateCount();
        }
        return newCount;
    }

    public Course getCourse(String courseCode) {
        for (Course course : this.getCourses()) {
            if (courseCode.equals(course.getCode())) {
                return course;
            }
        }

        return null;
    }
    
    public abstract List<Course> getCourses();
}
