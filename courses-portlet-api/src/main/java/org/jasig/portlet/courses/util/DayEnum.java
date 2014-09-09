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

/**
 *
 * @author Sengupta5
 */
public enum DayEnum {
   M(1), T(2), W(3), Th(4), F(5),Sa(6),Su(7);
   
   private final Integer value;
   
   private DayEnum(Integer value) 
   {
     this.value=value;
   }
   
   public Integer getValue()
   {
     return value;
   }
   
   public String getDescr()
   {
     if (this.value.equals(new Integer(1)))
         return "Monday";
     else if (this.value.equals(new Integer(2)))
         return "Tuesday";
     else if (this.value.equals(new Integer(3)))
         return "Wednesday";
     else if (this.value.equals(new Integer(4)))
         return "Thursday";
     else if (this.value.equals(new Integer(5)))
         return "Friday";
     else if (this.value.equals(new Integer(6)))
         return "Saturday";
     return "Sunday";
   }
}
