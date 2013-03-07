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

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds base functionality to the {@link CourseMeeting} object
 * 
 * @author Drew Wills
 */
public abstract class CourseMeetingWrapper {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final DateTimeFormatter SHORT_TIME_FORMAT = DateTimeFormat.shortTime();
    
    public abstract LocalTime getStartTime();
    
    public abstract LocalTime getEndTime();
    
    public abstract List<String> getDayIds();
    
    public String getFormattedMeetingTime() {

        StringBuilder rslt = new StringBuilder();

        LocalTime startTime = this.getStartTime();
        LocalTime endTime = this.getEndTime();
        
        /*
         * We need to tread carefully -- concrete DAOs are broadly allowed to 
         * pick-and-choose which data they provide. 
         */
        if (startTime != null) {
            try {
                SHORT_TIME_FORMAT.printTo(rslt, startTime);
                if (endTime != null) {
                    rslt.append(" - ");
                    SHORT_TIME_FORMAT.printTo(rslt, endTime);
                }
            }
            catch (IOException e) {
                logger.info("Failed to generate formatted string for course.startTime=" + startTime + " and course.endTime=" + endTime, e);
            }
        }

        return rslt.toString();
        
    }

    public String getFormattedMeetingDays() {

        StringBuilder rslt = new StringBuilder();

        List<String> days = this.getDayIds();
        
        /*
         * We need to tread carefully -- concrete DAOs are broadly allowed to 
         * pick-and-choose which data they provide. 
         */

        if (days != null && days.size() != 0) {
            for (String day : days) {
                rslt.append(day).append(", ");
            }
            // Remove the last ", "
            rslt.setLength(rslt.length() - 2);
        }
        
        return rslt.toString();
        
    }

}
