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

import org.jasig.portlet.courses.model.xml.CourseMeeting;
import org.jasig.portlet.courses.model.xml.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourseMeetingCompareByRoomAndStreetAddressTest {

	@Before
	public void setUp() throws Exception {
      CourseMeeting mikeCM = new CourseMeeting();
      CourseMeeting cm1 = new CourseMeeting();
      CourseMeeting cm2 = new CourseMeeting();
      CourseMeeting cm3 = new CourseMeeting();
      Location location1 = new Location();
      location1.setDisplayName("Van Hise");
      location1.setRoom("207");
      location1.setStreetAddress("1305 Linden Drive");
      Location location2 = new Location();
      location2.setDisplayName("Van Hise");
      location2.setRoom("207");
      location2.setStreetAddress("1305 Linden Drive");
      Location location3 = new Location();
      cm1.setLocation(location1);
      cm2.setLocation(location2);
      cm3.setLocation(location3);
      mikeCM.setLocation(location2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompareNullCourseMeetings() {
	  CourseMeeting senCM = null;
	  CourseMeeting colinCM = null;
      assertEquals("They are not equal",0, new CourseMeetingCompareByRoomAndStreetAddress().compare(senCM,colinCM));
	}
	@Test
	public void testCompareNullCourseMeetingWithNonNull() {
      CourseMeeting senCM = null;
      CourseMeeting mikeCM = new CourseMeeting();
      assertEquals("They are not equal",-1, new CourseMeetingCompareByRoomAndStreetAddress().compare(senCM,mikeCM));
      assertEquals("They are not equal",+1, new CourseMeetingCompareByRoomAndStreetAddress().compare(mikeCM,senCM));
    }
	@Test
	public void testCompareCourseMeetingsSame() {
      CourseMeeting cm1 = new CourseMeeting();
      CourseMeeting cm2 = new CourseMeeting();
      Location location1 = new Location();
      location1.setDisplayName("Van Hise");
      location1.setRoom("207");
      location1.setStreetAddress("1305 Linden Drive");
      cm1.setLocation(location1);
      Location location2 = new Location();
      location2.setDisplayName("Van Hise");
      location2.setRoom("207");
      location2.setStreetAddress("1305 Linden Drive");
      cm2.setLocation(location2);
      assertEquals("They are not equal",0, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm1,cm2));
    }
    @Test
    public void testCompareCourseMeetingsSameLocationDifferentRoom() {
      CourseMeeting cm1 = new CourseMeeting();
      CourseMeeting cm2 = new CourseMeeting();
      Location location1 = new Location();
      location1.setDisplayName("Van Hise");
      location1.setRoom("207");
      location1.setStreetAddress("1305 Linden Drive");
      cm1.setLocation(location1);
      Location location2 = new Location();
      location2.setDisplayName("Van Hise");
      location2.setRoom("307");
      location2.setStreetAddress("1305 Linden Drive");
      cm2.setLocation(location2);
      assertEquals("They are not equal",-1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm1,cm2));
      assertEquals("They are not equal",+1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm2,cm1));
    }
    @Test
    public void testCompareCourseMeetingsSameOneLocationNullBothWays() {
      CourseMeeting cm1 = new CourseMeeting();
      CourseMeeting cm2 = new CourseMeeting();
      Location location1 = new Location();
      location1.setDisplayName("Van Hise");
      location1.setRoom("207");
      location1.setStreetAddress("1305 Linden Drive");
      cm1.setLocation(location1);
      Location location2 = new Location();
      location2.setDisplayName("Van Hise");
      location2.setRoom("307");
      location2.setStreetAddress("1305 Linden Drive");
      assertEquals("They are not equal",-1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm2,cm1));
      assertEquals("They are not equal",+1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm1,cm2));
    }
    @Test
    public void testCompareCourseMeetingsSameStreetAddressDifferent() {
      CourseMeeting cm1 = new CourseMeeting();
      CourseMeeting cm2 = new CourseMeeting();
      Location location1 = new Location();
      location1.setDisplayName("Van Hise");
      location1.setRoom("207");
      location1.setStreetAddress("1305 Linden Drive");
      cm1.setLocation(location1);
      Location location2 = new Location();
      location2.setDisplayName("Van Hise");
      location2.setRoom("207");
      location2.setStreetAddress("1306 Linden Drive");
      cm2.setLocation(location2);
      assertEquals("They are not equal",+1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm2,cm1));
      assertEquals("They are not equal",-1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm1,cm2));
    }
    @Test
    public void testCompareCourseMeetingsSameStreetAddressDifferentFirstNull() {
      CourseMeeting cm1 = new CourseMeeting();
      CourseMeeting cm2 = new CourseMeeting();
      Location location1 = new Location();
      location1.setDisplayName("Van Hise");
      location1.setRoom("207");
      location1.setStreetAddress(null);
      cm1.setLocation(location1);
      Location location2 = new Location();
      location2.setDisplayName("Van Hise");
      location2.setRoom("207");
      location2.setStreetAddress("1305 Linden Drive");
      cm2.setLocation(location2);
      assertEquals("They are not equal",-1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm1,cm2));
    }
    @Test
    public void testCompareCourseMeetingsSameStreetAddressDifferentSecondNull() {
      CourseMeeting cm1 = new CourseMeeting();
      CourseMeeting cm2 = new CourseMeeting();
      Location location1 = new Location();
      location1.setDisplayName("Van Hise");
      location1.setRoom("207");
      location1.setStreetAddress("1305 Linden Drive");
      cm1.setLocation(location1);
      Location location2 = new Location();
      location2.setDisplayName("Van Hise");
      location2.setRoom("207");
      location2.setStreetAddress(null);
      cm2.setLocation(location2);
      assertEquals("They are not equal",+1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm1,cm2));
    }
    @Test
    public void testCompareCourseMeetingsSameRoomNull() {
        CourseMeeting cm1 = new CourseMeeting();
        CourseMeeting cm2 = new CourseMeeting();
        Location location1 = new Location();
        location1.setDisplayName("Van Hise");
        location1.setRoom("207");
        location1.setStreetAddress("1305 Linden Drive");
        cm1.setLocation(location1);
        Location location2 = new Location();
        location2.setDisplayName("Van Hise");
        location2.setStreetAddress("1306 Linden Drive");
        cm2.setLocation(location2);
        assertEquals("They are not equal",-1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm2,cm1));
        assertEquals("They are not equal",+1, new CourseMeetingCompareByRoomAndStreetAddress().compare(cm1,cm2));
      }
}
