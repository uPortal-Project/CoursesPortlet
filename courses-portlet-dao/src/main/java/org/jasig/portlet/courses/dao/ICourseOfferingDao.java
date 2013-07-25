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

package org.jasig.portlet.courses.dao;

import org.jasig.portlet.courses.model.catalog.xml.CourseList;
import org.jasig.portlet.courses.model.catalog.xml.CourseSection;
import org.jasig.portlet.courses.model.catalog.xml.Department;
import org.jasig.portlet.courses.model.catalog.xml.DepartmentList;
import org.jasig.portlet.courses.model.catalog.xml.FullCourseOffering;
import org.jasig.portlet.courses.model.catalog.xml.School;
import org.jasig.portlet.courses.model.catalog.xml.SchoolList;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;

public interface ICourseOfferingDao {
    
    public TermList getTerms(String schoolCode);
    
    public Term getCurrentTerm(String schoolCode);

    public SchoolList getSchools();
    
    public School getSchool(String schoolCode);
    
    public DepartmentList getDepartments(String schoolCode, String termCode);
    
    public Department getDepartment(String schoolCode, String departmentCode, String termCode);
    
    public CourseList getCourseOfferings(String schoolCode, String departmentCode, String termCode);
    
    public FullCourseOffering getCourseOffering(String courseCode, String termCode);
    
    public CourseSection getCourseSectionOffering(String courseCode, String sectionCode, String termCode);
    
}
