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
package org.jasig.portlet.courses.util;

import java.util.Comparator;

import org.jasig.portlet.courses.model.xml.personal.Course;

/**
 * Method will sort the Courses in Department Short Decr and Catalog Nbr
 * There are lots of Null checks because we are dealing with generated objects
 * TODO: Check into AspectJ for adding an Aspect for comparing.
 */
public class CourseCompareByDeptAndCatalog implements Comparator<Course> {

@Override
  public int compare(Course c1, Course c2) {
    //check for null Course objects eliminate possibility of NPE
	if((null == c1) && (null == c2)){ return 0;}
    if(null == c1){ return -1;}
    if(null == c2){ return +1;}
    
    //Are the Department objects null?
    if(c1.getCourseDepartment() == c2.getCourseDepartment()) {
      if(c1.getCode() == c2.getCode()) return 0;
      if(null == c1.getCode()) return -1;
      if(null == c2.getCode()) return +1;
      return (c1.getCode().compareTo(c2.getCode()));    
    }
    if(null == c1.getCourseDepartment()) return -1;
    if(null == c2.getCourseDepartment()) return +1;
    //Check for Department Name null
    if(c1.getCourseDepartment().getName() == c2.getCourseDepartment().getName()) {
      if(c1.getCode() == c2.getCode()) return 0;
      if(null == c1.getCode()) return -1;
      if(null == c2.getCode()) return +1;
      return (c1.getCode().compareTo(c2.getCode()));    
    }
    if(null == c1.getCourseDepartment().getName()) return -1;
    if(null == c2.getCourseDepartment().getName()) return +1;
    if(! ( c1.getCourseDepartment().getName().equals((c2.getCourseDepartment().getName())) ) ) {
      return (c1.getCourseDepartment().getName().compareTo(c2.getCourseDepartment().getName()));
    }
    else {
      if(c1.getCode() == c2.getCode()) return 0;
      if(null == c1.getCode()) return -1;
      if(null == c2.getCode()) return +1;
      return (c1.getCode().compareTo(c2.getCode()));    
    }
  }
}