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
package org.jasig.portlet.courses.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jasig.portlet.courses.model.xml.Department;
import org.jasig.portlet.courses.model.xml.personal.Course;
import org.junit.Test;

public class CourseCompareByDeptAndCatalogTest {

	@Test
	public void testCompare() {
		Department dept = new Department();
		dept.setCode("195");
		dept.setName("E ASIAN");
		Course senCourse = null;
		Course colinCourse = null;
		Course mikeCourse = new Course();
		mikeCourse.setCode("404");
		mikeCourse.setCourseDepartment(dept);
		Course anotherCourse = new Course();
		anotherCourse.setCode("301");
		anotherCourse.setCourseDepartment(dept);
		Course yetAnotherCourse = new Course();
		yetAnotherCourse.setCode("301");
		yetAnotherCourse.setCourseDepartment(dept);
		Course yetAnotherCourse2 = new Course();
		assertEquals("They are not equal",0, new CourseCompareByDeptAndCatalog().compare(senCourse,colinCourse));
		assertEquals("They are not equal",-1, new CourseCompareByDeptAndCatalog().compare(senCourse,mikeCourse));
		assertEquals("They are not equal",+1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,senCourse));
		assertEquals("They are not equal",0, new CourseCompareByDeptAndCatalog().compare(anotherCourse,yetAnotherCourse));
		assertEquals("They are not equal",+1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,yetAnotherCourse));
		assertEquals("They are not equal",-1, new CourseCompareByDeptAndCatalog().compare(yetAnotherCourse,mikeCourse));
		assertEquals("They are not equal",+1, new CourseCompareByDeptAndCatalog().compare(yetAnotherCourse,yetAnotherCourse2));
		assertEquals("They are not equal",-1, new CourseCompareByDeptAndCatalog().compare(yetAnotherCourse2,yetAnotherCourse));
	}
	@Test
	public void testCompareDepartmentSameCourseCodeDifferentSecondNull() {
		Department dept = new Department();
		dept.setCode("195");
		dept.setName("E ASIAN");
		Course mikeCourse = new Course();
		mikeCourse.setCode("404");
		mikeCourse.setCourseDepartment(dept);
		Course anotherCourse = new Course();
		anotherCourse.setCourseDepartment(dept);
		assertEquals("They are not equal",+1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}	
	@Test
	public void testCompareDepartmentSameCourseCodeDifferentFirstNull() {
		Department dept = new Department();
		dept.setCode("195");
		dept.setName("E ASIAN");
		Course mikeCourse = new Course();
		mikeCourse.setCourseDepartment(dept);
		Course anotherCourse = new Course();
		anotherCourse.setCode("404");
		anotherCourse.setCourseDepartment(dept);
		assertEquals("They are not equal",-1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}	
	@Test
	public void testCompareDepartmentSameCourseCodeNullForBoth() {
		Department dept = new Department();
		dept.setCode("195");
		dept.setName("E ASIAN");
		Course mikeCourse = new Course();
		mikeCourse.setCourseDepartment(dept);
		Course anotherCourse = new Course();
		anotherCourse.setCourseDepartment(dept);
		assertEquals("They are not equal",0, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}	
	@Test
	public void testCompareDepartmentCodeSameFirstNameNull() {
		Department dept = new Department();
		dept.setCode("195");
		Course mikeCourse = new Course();
		mikeCourse.setCourseDepartment(dept);
		Department dept2 = new Department();
		dept2.setCode("195");
		dept2.setName("E ASIAN");
		Course anotherCourse = new Course();
		anotherCourse.setCourseDepartment(dept2);
		assertEquals("They are not equal",-1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}	
	@Test
	public void testCompareDepartmentCodeSameSecondNameNull() {
		Department dept = new Department();
		dept.setCode("195");
		dept.setName("E ASIAN");
		Course mikeCourse = new Course();
		mikeCourse.setCourseDepartment(dept);
		Department dept2 = new Department();
		dept2.setCode("195");
		Course anotherCourse = new Course();
		anotherCourse.setCourseDepartment(dept2);
		assertEquals("They are not equal",+1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}	
	@Test
	public void testCompareDepartmentCodeSameBothNamesNull() {
		Department dept = new Department();
		dept.setCode("195");
		Course mikeCourse = new Course();
		mikeCourse.setCourseDepartment(dept);
		Department dept2 = new Department();
		dept2.setCode("195");
		Course anotherCourse = new Course();
		anotherCourse.setCourseDepartment(dept2);
		assertEquals("They are not equal",0, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}	
	@Test
	public void testCompareDepartmentCodeSameBothNamesNullFirstCourseCodeNull() {
		Department dept = new Department();
		dept.setCode("195");
		Course mikeCourse = new Course();
		mikeCourse.setCourseDepartment(dept);
		Department dept2 = new Department();
		dept2.setCode("195");
		Course anotherCourse = new Course();
		anotherCourse.setCourseDepartment(dept2);
		anotherCourse.setCode("399");
		assertEquals("They are not equal",-1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}	
	@Test
	public void testCompareDepartmentCodeSameBothNamesNullSecondCourseCodeNull() {
		Department dept = new Department();
		dept.setCode("195");
		Course mikeCourse = new Course();
		mikeCourse.setCourseDepartment(dept);
		mikeCourse.setCode("399");
		Department dept2 = new Department();
		dept2.setCode("195");
		Course anotherCourse = new Course();
		anotherCourse.setCourseDepartment(dept2);
		assertEquals("They are not equal",+1, new CourseCompareByDeptAndCatalog().compare(mikeCourse,anotherCourse));
	}
	@Test
	public void testCompareListOfCourses() {
		List<Course> courses = new ArrayList<Course>();
		courses.add(new Course());
		courses.get(0).setCode("601");
		courses.get(0).setCourseDepartment(new Department());
		courses.get(0).getCourseDepartment().setName("M E");
		courses.add(new Course());
		courses.get(1).setCode("342");
		courses.get(1).setCourseDepartment(new Department());
		courses.get(1).getCourseDepartment().setName("M E");
		courses.add(new Course());
		courses.get(2).setCode("397");
		courses.get(2).setCourseDepartment(new Department());
		courses.get(2).getCourseDepartment().setName("E P D");
		courses.add(new Course());
		courses.get(3).setCode("314");
		courses.get(3).setCourseDepartment(new Department());
		courses.get(3).getCourseDepartment().setName("M E");
		courses.add(new Course());
		courses.get(4).setCode("364");
		courses.get(4).setCourseDepartment(new Department());
		courses.get(4).getCourseDepartment().setName("M E");
		System.out.println("Pre-Sort Courses: "+courses);
		System.out.println("Pre-Sort Courses 0: "+courses.get(0));
		System.out.println("Pre-Sort Courses 1: "+courses.get(1));
		Collections.sort(courses, new CourseCompareByDeptAndCatalog());
		System.out.println("Post-Sort Courses: "+courses);
		System.out.println("Post-Sort Courses 0: "+courses.get(0));
		System.out.println("Post-Sort Courses 1: "+courses.get(1));
		assertEquals("Position 1 is not 314","314", courses.get(1).getCode());
	}}
