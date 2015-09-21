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
package org.jasig.portlet.courses.model.xml;

import java.text.DateFormat;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Adds base functionality to the {@link CourseMeeting} object
 * 
 * @author Drew Wills
 */
public abstract class CourseMeetingWrapper {
    
    private static final DateFormat DATE_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);
    
    public abstract XMLGregorianCalendar getStartTime();
    
    public abstract XMLGregorianCalendar getEndTime();
    
    public abstract List<String> getDayIds();
    
    public String getFormattedMeetingTime() {

        StringBuilder rslt = new StringBuilder();

        XMLGregorianCalendar startTime = this.getStartTime();
        XMLGregorianCalendar endTime = this.getEndTime();
        
        /*
         * We need to tread carefully -- concrete DAOs are broadly allowed to 
         * pick-and-choose which data they provide. 
         */
        
        if (startTime != null) {
            rslt.append(DATE_FORMAT.format(startTime.toGregorianCalendar().getTime()));
            if (endTime != null) {
                rslt.append(" - ").append(DATE_FORMAT.format(endTime.toGregorianCalendar().getTime()));
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
