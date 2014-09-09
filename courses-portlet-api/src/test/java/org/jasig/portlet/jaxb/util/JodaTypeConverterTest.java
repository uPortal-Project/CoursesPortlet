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
package org.jasig.portlet.jaxb.util;

import static org.junit.Assert.assertEquals;

import java.util.TimeZone;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JodaTypeConverterTest {
    private static DateTimeZone defaultDTZ;
    private static TimeZone defaultTZ;
    
    @BeforeClass
    public static void setupTZ() {
        defaultDTZ = DateTimeZone.getDefault();
        defaultTZ = TimeZone.getDefault();
        
        DateTimeZone.setDefault(DateTimeZone.UTC);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    
    @AfterClass
    public static void resetTZ() {
        DateTimeZone.setDefault(defaultDTZ);
        TimeZone.setDefault(defaultTZ);
    }
    
    @Test
    public void testLocalTZTimeConversion() {
        final LocalTime time = JodaTypeConverter.parseTime("09:30:10-06:00");
        final String timeStr = JodaTypeConverter.printTime(time);
        assertEquals("15:30:10Z", timeStr);
    }
    
    @Test
    public void testNoTZTimeConversion() {
        final LocalTime time = JodaTypeConverter.parseTime("09:30:10");
        final String timeStr = JodaTypeConverter.printTime(time);
        assertEquals("09:30:10Z", timeStr);
    }
    
    @Test
    public void testTimeConversion() {
        final LocalTime time = JodaTypeConverter.parseTime("15:30:10Z");
        final String timeStr = JodaTypeConverter.printTime(time);
        assertEquals("15:30:10Z", timeStr);
    }
}
