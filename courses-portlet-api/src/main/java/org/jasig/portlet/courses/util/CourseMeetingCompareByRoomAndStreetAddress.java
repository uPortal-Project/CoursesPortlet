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
import org.jasig.portlet.courses.model.xml.CourseMeeting;
public class CourseMeetingCompareByRoomAndStreetAddress implements
               Comparator<CourseMeeting> {
/**
* Method will sort the Courses Meeting Location 
*/
@Override
  public int compare(CourseMeeting cm1, CourseMeeting cm2) {
    if(cm1 == cm2) return 0;
    if(null == cm1) return -1;
    if(null == cm2) return +1;
    if(cm1.getLocation() == cm2.getLocation()) return 0;
    if(null == cm1.getLocation()) return -1;
    if(null == cm2.getLocation()) return +1;
    if(!(cm1.getLocation().getRoom() == cm2.getLocation().getRoom())) {
      if(null == cm1.getLocation().getRoom()) return -1;
      if(null == cm2.getLocation().getRoom()) return +1;
        return (cm1.getLocation().getRoom().compareTo(cm2.getLocation().getRoom()));
    } else { 
    	if(cm1.getLocation().getStreetAddress() == cm2.getLocation().getStreetAddress()) return 0;
          if(null == cm1.getLocation().getStreetAddress()) return -1;
          if(null == cm2.getLocation().getStreetAddress()) return +1;
          return (cm1.getLocation().getStreetAddress().compareTo(cm2.getLocation().getStreetAddress()));
        }
    }
}
