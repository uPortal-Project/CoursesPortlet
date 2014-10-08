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

import static org.junit.Assert.*;

import org.jasig.portlet.courses.model.xml.CourseSection;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourseSectionCodeComparatorTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testCompare() {
    CourseSection courseSection1 = null;
    CourseSection courseSection2 = null;
    CourseSection courseSection3 = new CourseSection();
    CourseSection courseSection4 = new CourseSection();
    courseSection3.setCode("010");
    courseSection4.setCode("010");
    //null test
    assertEquals("They are not equal",0,new CourseSectionCodeComparator().compare(courseSection1,courseSection2));
    //null vs not null
    assertEquals("They are not equal",-1,new CourseSectionCodeComparator().compare(courseSection1,courseSection3));
    //not null vs null
    assertEquals("They are not equal",+1,new CourseSectionCodeComparator().compare(courseSection3,courseSection1));
    //same
    assertEquals("They are not equal",0,new CourseSectionCodeComparator().compare(courseSection3,courseSection4));
    courseSection4.setCode("011");
    //different
    assertEquals("They are not equal",-1,new CourseSectionCodeComparator().compare(courseSection3,courseSection4));
    //different
    assertEquals("They are not equal",+1,new CourseSectionCodeComparator().compare(courseSection4,courseSection3));
    courseSection1 = new CourseSection();
    courseSection2 = new CourseSection();
    assertEquals("They are not equal",0,new CourseSectionCodeComparator().compare(courseSection1,courseSection2));
    courseSection1.setCode("311");
    assertEquals("They are not equal",+1,new CourseSectionCodeComparator().compare(courseSection1,courseSection2));
    courseSection1.setCode(null);
    courseSection2.setCode("311");
    assertEquals("They are not equal",-1,new CourseSectionCodeComparator().compare(courseSection1,courseSection2));
  }

}
