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
package org.jasig.portlet.courses.mvc.wrapper;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.courses.model.xml.CourseMeeting;
import org.jasig.portlet.courses.model.xml.CourseSection;
import org.jasig.portlet.courses.service.IURLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.portlet.PortletPreferences;
import java.util.*;

/**
 * User: Sengupta5
 * Date: 8/26/13
 * Time: 11:41 AM
 * This Wrapper gets All the Course Meeting from the Course Section and group by Location.
 */
public class CourseSectionMeetingWrapper {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  // Matches meeting location displayName of each meeting type without a location
  private static String[] meetingTypesWithNoLocation = new String[]{
    "TBD", "PENDING", "ONLINE", "OFF CAMPUS"
  };

  private static final String ENROLLEDCLASSESGRADESFNAME_PREF = "enrolledClassesFname";
  private static final String ENROLLEDCLASSESWINDOWSTATE_PREF = "enrolledClassesWindowState";
  private static final String ENROLLEDCLASSESFIXEDPARAM_PREF = "enrolledClassesWindowFixedParameterString";
  private static final String ENROLLEDCLASSESNATIVEMAPBASESURL_PREF = "enrolledClassesNativeMapBaseURL";
  private static final String ENROLLEDCLASSESNATIVEMAPZOOMINDEX_PREF = "enrolledClassesNativeMapZIndex";

  private CourseSection courseSection;
  private PortletPreferences portletPreferences;
  private boolean mobile;
  private String termCode;
  private IURLService urlService;

  public String getTermCode() {
    return termCode;
  }

  public void setTermCode(String termCode) {
    this.termCode = termCode;
  }

  public boolean isMobile() {
    return mobile;
  }

  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }

  public PortletPreferences getPortletPreferences() {
    return portletPreferences;
  }

  public void setPortletPreferences(PortletPreferences portletPreferences) {
    this.portletPreferences = portletPreferences;
  }

  public IURLService getUrlService() { return urlService;  }

  public void setUrlService(IURLService urlService) {
    this.urlService = urlService;
  }

  private Map<LocationWrapper,List<CourseMeeting>> locationClassMeetingMap;
  private Map<LocationWrapper,List<CourseMeeting>> locationExamMeetingMap;

  public Map<LocationWrapper, List<CourseMeeting>> getLocationExamMeetingMap() {
    return locationExamMeetingMap;
  }

  public void setLocationExamMeetingMap(Map<LocationWrapper, List<CourseMeeting>> locationExamMeetingMap) {
    this.locationExamMeetingMap = locationExamMeetingMap;
  }

  public void setLocationClassMeetingMap(Map<LocationWrapper, List<CourseMeeting>> locationClassMeetingMap) {
    this.locationClassMeetingMap = locationClassMeetingMap;
  }

  public CourseSection getCourseSection() {
    return courseSection;
  }

  public void setCourseSection(CourseSection courseSection) {
    this.courseSection = courseSection;
  }

  public Map getLocationClassMeetingMap() {
    return locationClassMeetingMap;
  }

  public CourseSectionMeetingWrapper() {};


  public CourseSectionMeetingWrapper(CourseSection courseSection,PortletPreferences portletPreferences,boolean mobile,String termCode,IURLService urlService) {
      this.setUrlService(urlService);
      this.setCourseSection(courseSection);
      this.setPortletPreferences(portletPreferences);
      this.setMobile(mobile);
      this.setTermCode(termCode);
      this.setLocationClassMeetingMap(setClassMeetingMap(courseSection));
      this.setLocationExamMeetingMap(setExamMeetingMap(courseSection));
  }

  private Map<LocationWrapper,List<CourseMeeting>> setClassMeetingMap(CourseSection courseSection)
  {
    String displayName="";
    String roomNo="";
    String streetAddress="";
    List<CourseMeeting> courseMeetings=null;
    logger.debug("Setting ClassMeeting Map......");
    Map<LocationWrapper,List<CourseMeeting>> locationMeetMap=new HashMap<LocationWrapper,List<CourseMeeting>>();
      for(CourseMeeting courseMeetingObj:courseSection.getCourseMeetings())
      {
        logger.debug("CourseMeeting Type........"+courseMeetingObj.getType());
        if (courseMeetingObj.getType().toUpperCase().equals("CLASS"))
        {
          if (logger.isDebugEnabled())
          {
          logger.debug("courseMeetingObj.getLocation().getDisplayName()........"+courseMeetingObj.getLocation().getDisplayName());
          logger.debug("courseMeetingObj.getLocation().getStreetAddress()........"+courseMeetingObj.getLocation().getStreetAddress());
          logger.debug("courseMeetingObj.getLocation().getRoom()........"+courseMeetingObj.getLocation().getRoom());
          logger.debug("courseMeetingObj.getLocation().getIdentifier()........"+courseMeetingObj.getLocation().getIdentifier());
          logger.debug("courseMeetingObj.getLocation().getLatitude()........"+courseMeetingObj.getLocation().getLatitude());
          logger.debug("courseMeetingObj.getLocation().getLongitude()........"+courseMeetingObj.getLocation().getLongitude());
          logger.debug("TermCode........"+getTermCode());
          logger.debug("isMobile........"+isMobile());
          }
          if ((!courseMeetingObj.getLocation().getDisplayName().equals(displayName)) || (!courseMeetingObj.getLocation().getStreetAddress().equals(streetAddress)) || (!courseMeetingObj.getLocation().getRoom().equals(roomNo)))
          {
            logger.debug("Class Location Not Matching......");
            LocationWrapper locationWrapper=null;
            if (StringUtils.indexOfAny(courseMeetingObj.getLocation().getDisplayName(),
                                       meetingTypesWithNoLocation) == 0)
            {
              logger.debug("Location DisplayName is " + courseMeetingObj.getLocation().getDisplayName());
              locationWrapper=new LocationWrapper(courseMeetingObj.getLocation().getDisplayName(),"","",0,0,"","");
            }
            else
            {
              String locationUrl="";
              if (!isMobile())
              {
                Map<String,String> params=new HashMap<String,String>();
                params.put("pP_term",getTermCode());
                params.put("pP_buildingName", courseMeetingObj.getLocation().getDisplayName());
                locationUrl = this.getUrlService().getOtherPortletURL(getPortletPreferences(),
                  ENROLLEDCLASSESGRADESFNAME_PREF,
                  ENROLLEDCLASSESWINDOWSTATE_PREF,
                  ENROLLEDCLASSESFIXEDPARAM_PREF,
                  params);
              }
              else
              {
                locationUrl=this.getUrlService().getNativeMapUrl(getPortletPreferences(), ENROLLEDCLASSESNATIVEMAPBASESURL_PREF, courseMeetingObj.getLocation().getRoom(), courseMeetingObj.getLocation().getDisplayName(), courseMeetingObj.getLocation().getStreetAddress(), new Double(courseMeetingObj.getLocation().getLongitude()).toString(), new Double(courseMeetingObj.getLocation().getLatitude()).toString(), ENROLLEDCLASSESNATIVEMAPZOOMINDEX_PREF);
              }
              logger.debug("Location URL.....in CLASS"+locationUrl);
              locationWrapper=new LocationWrapper(courseMeetingObj.getLocation().getDisplayName(),courseMeetingObj.getLocation().getIdentifier(),courseMeetingObj.getLocation().getStreetAddress(),courseMeetingObj.getLocation().getLatitude(),courseMeetingObj.getLocation().getLongitude(),courseMeetingObj.getLocation().getRoom(),locationUrl) ;
            }
            courseMeetings=new ArrayList<CourseMeeting>();
            locationMeetMap.put(locationWrapper,courseMeetings);
            displayName=courseMeetingObj.getLocation().getDisplayName();
            streetAddress=courseMeetingObj.getLocation().getStreetAddress();
            roomNo=courseMeetingObj.getLocation().getRoom();
            courseMeetings.add(courseMeetingObj);
            logger.debug("Class Location Not Matching......Done");
          }
          else
          {
            logger.debug("Class Location Matching......");
            courseMeetings.add(courseMeetingObj);
          }
        }
      }
    if (locationMeetMap.isEmpty())
      logger.debug("CLASS MAP IS EMPTY !!!!!");

    return locationMeetMap;
  }

  private Map<LocationWrapper,List<CourseMeeting>> setExamMeetingMap(CourseSection courseSection)
  {
    String displayName=null;
    String roomNo=null;
    String streetAddress=null;
    List<CourseMeeting> courseMeetings=null;
    courseMeetings=new ArrayList<CourseMeeting>();
    logger.debug("Setting ExamMeeting Map......");
    Map<LocationWrapper,List<CourseMeeting>> locationMeetMap=new HashMap<LocationWrapper,List<CourseMeeting>>();
    for(CourseMeeting courseMeetingObj:courseSection.getCourseMeetings())
    {
      logger.debug("CourseMeeting Type........"+courseMeetingObj.getType());

      if (courseMeetingObj.getType().toUpperCase().equals("EXAM"))
      {
        logger.debug("courseMeetingObj.getLocation().getDisplayName()........"+courseMeetingObj.getLocation().getDisplayName());
        logger.debug("courseMeetingObj.getLocation().getStreetAddress()........"+courseMeetingObj.getLocation().getStreetAddress());
        logger.debug("courseMeetingObj.getLocation().getRoom()........"+courseMeetingObj.getLocation().getRoom());
        logger.debug("courseMeetingObj.getLocation().getIdentifier()........"+courseMeetingObj.getLocation().getIdentifier());
        logger.debug("courseMeetingObj.getLocation().getLatitude()........"+courseMeetingObj.getLocation().getLatitude());
        logger.debug("courseMeetingObj.getLocation().getLongitude()........"+courseMeetingObj.getLocation().getLongitude());
        logger.debug("isMobile........"+isMobile());

        if ((courseMeetingObj.getLocation()!=null)&& (courseMeetingObj.getLocation().getStreetAddress()!=null)&& (courseMeetingObj.getLocation().getRoom()!=null)&&(!courseMeetingObj.getLocation().getDisplayName().equals(displayName)) && (!courseMeetingObj.getLocation().getStreetAddress().equals(streetAddress)) && (!courseMeetingObj.getLocation().getRoom().equals(roomNo)))
        {
          logger.debug("Exam Location Not Matching......");
          LocationWrapper locationWrapper=null;
          if (StringUtils.indexOfAny(courseMeetingObj.getLocation().getDisplayName(),
                                     meetingTypesWithNoLocation) == 0)
          {
             logger.debug("Location DisplayName is " + courseMeetingObj.getLocation().getDisplayName());
             locationWrapper=new LocationWrapper(courseMeetingObj.getLocation().getDisplayName(),"","",0,0,"","");
          }
          else
          {
            String locationUrl="";
            locationUrl=this.getUrlService().getNativeMapUrl(getPortletPreferences(), ENROLLEDCLASSESNATIVEMAPBASESURL_PREF, courseMeetingObj.getLocation().getRoom(), courseMeetingObj.getLocation().getDisplayName(), courseMeetingObj.getLocation().getStreetAddress(), new Double(courseMeetingObj.getLocation().getLongitude()).toString(), new Double(courseMeetingObj.getLocation().getLatitude()).toString(), ENROLLEDCLASSESNATIVEMAPZOOMINDEX_PREF);
            logger.debug("Location URL.....in EXAM"+locationUrl);
            locationWrapper=new LocationWrapper(courseMeetingObj.getLocation().getDisplayName(),courseMeetingObj.getLocation().getIdentifier(),courseMeetingObj.getLocation().getStreetAddress(),courseMeetingObj.getLocation().getLatitude(),courseMeetingObj.getLocation().getLongitude(),courseMeetingObj.getLocation().getRoom(),locationUrl) ;
          }
          courseMeetings=new ArrayList<CourseMeeting>();
          locationMeetMap.put(locationWrapper,courseMeetings);
          displayName=courseMeetingObj.getLocation().getDisplayName();
          streetAddress=courseMeetingObj.getLocation().getStreetAddress();
          roomNo=courseMeetingObj.getLocation().getRoom();
          logger.debug("Exam Location Not Matching......Done");
          courseMeetings.add(courseMeetingObj);
        }
        else
        {
          logger.debug("Exam Location Matching......");
          courseMeetings.add(courseMeetingObj);
        }
      }
    }
    if (locationMeetMap.isEmpty())
       logger.debug("EXAM MAP IS EMPTY !!!!!");
    return locationMeetMap;
  }

  public void printCourseSectionWrapperList(List<CourseSectionMeetingWrapper> courseSectionMeetingWrapperList) {
    for (CourseSectionMeetingWrapper courseSectionMeetingWrapper : courseSectionMeetingWrapperList)
    {
      logger.debug("COURSE SECTION>>>>>>>>>>>>>>>");
      logger.debug(courseSectionMeetingWrapper.getCourseSection().getType()+" "+ courseSectionMeetingWrapper.getCourseSection().getCode());
      Map<LocationWrapper,List<CourseMeeting>>  classMeetingMap= courseSectionMeetingWrapper.getLocationClassMeetingMap();
      Map<LocationWrapper,List<CourseMeeting>>  examMeetingMap= courseSectionMeetingWrapper.getLocationExamMeetingMap();

      Set<LocationWrapper> locationWrapperSet=classMeetingMap.keySet();
      logger.debug("DUMPING ALL CLASSES>>>>>>>>>>>>>>>");
      for(LocationWrapper locationWrapper:locationWrapperSet)
      {
        List<CourseMeeting> courseMeetings=classMeetingMap.get(locationWrapper);
        {
          for(CourseMeeting courseMeeting :courseMeetings)
          {
            logger.debug(courseMeeting.getType());
            logger.debug(courseMeeting.getFormattedMeetingTime()+"......"+courseMeeting.getDayIds());
          }
        }
        logger.debug(locationWrapper.getDisplayName()+"....."+locationWrapper.getRoom()+"....."+locationWrapper.getStreetAddress());
      }
      logger.debug("DUMPING ALL EXAMS>>>>>>>>>>>>>>>");
      locationWrapperSet=examMeetingMap.keySet();
      for(LocationWrapper locationWrapper:locationWrapperSet)
      {
        List<CourseMeeting> courseMeetings=examMeetingMap.get(locationWrapper);
        {
          for(CourseMeeting courseMeeting :courseMeetings)
          {
            logger.debug(courseMeeting.getType());
            logger.debug(courseMeeting.getFormattedMeetingTime()+"......"+courseMeeting.getDayIds());
          }
        }
        logger.debug(locationWrapper.getDisplayName()+"....."+locationWrapper.getRoom()+"....."+locationWrapper.getStreetAddress());
      }

    }
  }
}
