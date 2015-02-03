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
package org.jasig.portlet.courses.mvc.wrapper;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jasig.portlet.courses.model.xml.AdditionalInfo;
import org.jasig.portlet.courses.model.xml.CourseMeeting;
import org.jasig.portlet.courses.model.xml.CourseSection;
import org.jasig.portlet.courses.model.xml.Location;
import org.jasig.portlet.courses.model.xml.personal.Course;
import org.jasig.portlet.courses.util.DayEnum;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CourseSectionWrapper
 * <p>
 * Wraps a Course, CourseSection and CourseMeeting for easy conversion to JSON
 * and reference within JSPs.
 *
 * @author Sengupta5@wisc.edu
 * @author CPKennedy2@wisc.edu
 */
public class CourseSectionWrapper {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @JsonIgnore private CourseMeeting courseMeeting;
  @JsonIgnore private CourseSection courseSection;
  @JsonIgnore private Course course;
  private String startTime;
  private String startDateDisplay;
  private String timeRangeDisplay;
  @JsonIgnore private DateMidnight startDateObj;
  @JsonIgnore public static DateTimeFormatter monthDescrFormat = DateTimeFormat.forPattern("MMM");
  @JsonIgnore public static DateTimeFormatter startTimeFormat = DateTimeFormat.forPattern("H:mm");
  @JsonIgnore public static DateTimeFormatter amPmFormat = DateTimeFormat.forPattern("h:mm a");
  @JsonIgnore public static DateTimeFormatter dateDisplayFormat = DateTimeFormat.forPattern("MMM d");
  private Integer duration;
  private Integer scheduledDay;
  private Integer dayId;
  private String day;
  private String dayDescr;
  private String url;
  private String sessionCode;
  private String sessionStartEndDate;

  private String room;
  private String locationDisplayName;
  private String streetAddress;

  private String courseDescr;
  private String courseTitle;
  private String courseCode;

  private String meetingDescr;

  @JsonIgnore private static Map<String, DayEnum> dayMap;
  @JsonIgnore private static DayEnum[] dayArr = {
    DayEnum.Su,
    DayEnum.M ,
    DayEnum.T ,
    DayEnum.W ,
    DayEnum.Th,
    DayEnum.F ,
    DayEnum.Sa,
    DayEnum.Su   // A second time because JodaTime uses 7 as Sunday
  };

  static {
    Map<String, DayEnum> m = new HashMap();
    m.put(DayEnum.Su.toString(), DayEnum.Su);
    m.put(DayEnum.M.toString() , DayEnum.M );
    m.put(DayEnum.T.toString() , DayEnum.T );
    m.put(DayEnum.W.toString() , DayEnum.W );
    m.put(DayEnum.Th.toString(), DayEnum.Th);
    m.put(DayEnum.F.toString() , DayEnum.F );
    m.put(DayEnum.Sa.toString(), DayEnum.Sa);

    dayMap = Collections.unmodifiableMap(m);
  }


  public CourseSectionWrapper() {}

  /* For "other" courses without a meeting time
   * <p>
   * Used by the Schedule Grid
   *
   * @param course  Reference Course.  @see #setCourse(Course)
   * @param section Reference section, which is a member of course
   *                @see #setCourseSection(CourseSection)
   * @param url     The URL for the detail view of course and section
   */
  public CourseSectionWrapper(Course course,
                              CourseSection section,
                              String url)
  {
    this.setUrl(url);
    this.setCourse(course);
    this.setCourseSection(section);
    this.setSessionCode(getCourseSectionAdditionalInfoValue(section, "sessionCode"));
    this.setSessionStartEndDate(getCourseSectionAdditionalInfoValue(section, "sessionStartEndDate"));

  }

  /*
   * For individual meeting days
   * <p>
   * Used by the Schedule Grid
   *
   * @param course  Reference Course.  {@link #setCourse(Course)}
   * @param section Reference section, which is a member of course
   *                {@link #setCourseSection(CourseSection)}
   * @param meeting Reference meeting, which is a member of section
   *                {@link #setCourseMeeting(CourseMeeting)}
   * @param day     Day reference, from meeting.  Is translated to a
   *                day of week and name.<br/>
   *                {@link #setDay(DayEnum)}<br/>
   *                {@link DayEnum}
   * @param url     The URL for the detail view of course and section
   */
  public CourseSectionWrapper(Course course,
                              CourseSection section,
                              CourseMeeting meeting,
                              String day,
                              String url)
  {
    this.setDay(dayMap.get(day));
    this.setUrl(url);
    this.setCourse(course);
    this.setCourseSection(section);
    this.setCourseMeeting(meeting);
    this.setSessionCode(getCourseSectionAdditionalInfoValue(section, "sessionCode"));
    this.setSessionStartEndDate(getCourseSectionAdditionalInfoValue(section, "sessionStartEndDate"));
  }

  /**
   * For individual meeting days
   * <p>
   * Used by the Final Exam Grid
   *
   * @param course  Reference Course.  {@link #setCourse(Course)}
   * @param section Reference section, which is a member of course
   *                {@link #setCourseSection(CourseSection)}
   * @param meeting Reference meeting, which is a member of section
   *                {@link #setCourseMeeting(CourseMeeting)}
   *
   *                Used to set the Start date reference,
   *                Is used to set day of week and name.<br/>
   *                {@link #setDay(DayEnum)}<br/>
   *                {@link DayEnum}
   * @param url     The URL for the detail view of course and section
   */
  public CourseSectionWrapper(Course course,
                              CourseSection section,
                              CourseMeeting meeting,
                              String url)
  {
    DateMidnight date = meeting.getStartDate();
    this.setStartDateObj(date);
    this.setDay(dayArr[date.getDayOfWeek()]);
    this.setUrl(url);
    this.setCourse(course);
    this.setCourseSection(section);
    this.setCourseMeeting(meeting);
    this.setSessionCode(getCourseSectionAdditionalInfoValue(section, "sessionCode"));
    this.setSessionStartEndDate(getCourseSectionAdditionalInfoValue(section, "sessionStartEndDate"));
  }

  /**
   * @return the courseMeeting
   */
  public CourseMeeting getCourseMeeting() {
    return courseMeeting;
  }

  /**
   * Sets the courseMeeting object
   * <p/>
   * Also sets the formatted startTime and duration of the courseMeeting
   *
   * @see #startTimeFormat
   * @see #getStartTime()
   * @see #getDuration()
   *
   * @param courseMeeting the courseMeeting to set
   */
  public void setCourseMeeting(CourseMeeting courseMeeting) {
    LocalTime startTime = courseMeeting.getStartTime();
    LocalTime endTime = courseMeeting.getEndTime();
    this.startTime = startTimeFormat.print(startTime);
    this.duration = (Minutes.minutesBetween(startTime, endTime)).getMinutes();

    this.timeRangeDisplay = amPmFormat.print(startTime) + " to " + amPmFormat.print(endTime);

    Location location = courseMeeting.getLocation();
    this.room = location.getRoom();
    this.locationDisplayName = location.getDisplayName();
    this.streetAddress = location.getStreetAddress();

    this.courseMeeting = courseMeeting;
  }

  /**
   * @return the courseSection
   */
  public CourseSection getCourseSection() {
    return courseSection;
  }

  /**
   * Sets the course object
   * <p/>
   * Also sets the meetingDescr from the courseSection object
   *
   * @see #getMeetingDescr()
   *
   * @param courseSection the courseSection to set
   */
  public void setCourseSection(CourseSection courseSection) {
    this.meetingDescr = courseSection.getType() + " " + courseSection.getCode();
    this.courseSection = courseSection;
  }

  /**
   * @return the course
   */
  public Course getCourse() {
    return course;
  }

  /**
   * Sets the courseSection object
   * <p/>
   * Also sets the courseCode, courseDescr and courseTitle from the course object
   *
   * @see #getCourseCode()
   * @see #getCourseDescr()
   * @see #getCourseTitle()
   *
   * @param course the course to set
   */
  public void setCourse(Course course) {
    this.courseCode = course.getCode();
    this.courseDescr = course.getCourseDepartment().getName();
    this.courseTitle = course.getTitle();

    this.course = course;
  }

  @JsonIgnore
  public int getExamStartSunday() {
    if( this.getStartDateObj() == null ){
      return 0;
    }

    DateMidnight date = this.getStartDateObj();

    if( date.getDayOfWeek() == DateTimeConstants.SUNDAY ){
      return date.getDayOfMonth();
    }

    return date.withDayOfWeek(DateTimeConstants.SUNDAY)
           .minusWeeks(1)
           .getDayOfMonth();
  }
  @JsonIgnore
  public String getMonthDescrOfStart() {
    if( this.getStartDateObj() == null ){
      return "";
    }
    return monthDescrFormat.print(this.getStartDateObj());
  }

  public static Comparator<CourseSectionWrapper> FinalExamComparator
                          = new Comparator<CourseSectionWrapper>() {
 
	    public int compare(CourseSectionWrapper fe1, CourseSectionWrapper fe2) {
       int i= fe1.courseMeeting.getStartDate().compareTo(fe2.getCourseMeeting().getStartDate());
       if (i!=0) return i;
       return fe1.courseMeeting.getStartTime().compareTo(fe2.getCourseMeeting().getStartTime());
	    }
 
	  };
  /**
   * For sorting {@link CourseSectionWrapper}s by {@link org.jasig.portlet.courses.model.xml.CourseMeeting#getStartTime()}
   */
  public static Comparator<CourseSectionWrapper> ClassScheduleComparator
                          = new Comparator<CourseSectionWrapper>() {
 
	    public int compare(CourseSectionWrapper fe1, CourseSectionWrapper fe2) {
       int i= fe1.getDayId().compareTo(fe2.getDayId());
       if (i!=0) return i;
       return fe1.courseMeeting.getStartTime().compareTo(fe2.getCourseMeeting().getStartTime());
	    }

	  };
  /**
   * For sorting {@link CourseSectionWrapper}s first by {@link #getCourseDescr()},
   * then by {@link org.jasig.portlet.courses.model.xml.CourseSection#getCode()}
   */
  public static Comparator<CourseSectionWrapper> OtherCourseComparator =
    new Comparator<CourseSectionWrapper>() {
      @Override
      public int compare(CourseSectionWrapper o1, CourseSectionWrapper o2) {
        int courseDescr = o1.getCourseDescr().compareTo(o2.getCourseDescr());
        return courseDescr != 0 ? courseDescr :
               o1.getCourseSection().getCode().compareTo(o2.getCourseSection().getCode());
      }
    };
  /**
   * @return the startTime
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   * @param startTime the startTime to set
   */
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public void setDay(DayEnum d) {
    this.setDay(d.toString());
    this.setDayId(d.getValue());
    this.setDayDescr(d.getDescr());
  }

  /**
   * @return the dayId
   */
  public Integer getDayId() {
    return dayId;
  }

  /**
   * @param dayId the dayId to set
   */
  public void setDayId(Integer dayId) {
    this.dayId = dayId.equals(new Integer(7)) ? new Integer(0) : dayId;
    this.scheduledDay = this.dayId;
  }

  /**
   * @return the day
   */
  public String getDay() {
    return day;
  }

  /**
   * @param day the day to set
   */
  public void setDay(String day) {
    this.day = day;
  }

  /**
   * @return the dayDescr
   */
  public String getDayDescr() {
    return dayDescr;
  }

  /**
   * @param dayDescr the dayDescr to set
   */
  public void setDayDescr(String dayDescr) {
    this.dayDescr = dayDescr;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMeetingDescr() {
    return meetingDescr;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public String getCourseDescr() {
    return courseDescr;
  }

  public Integer getScheduledDay() {
    return scheduledDay;
  }

  public Integer getDuration() {
    return duration;
  }

  public String getCourseTitle() {
    return courseTitle;
  }

  public DateMidnight getStartDateObj() {
    return startDateObj;
  }

  public void setStartDateObj(DateMidnight startDateObj) {
    this.startDateDisplay = dateDisplayFormat.print(startDateObj);
    this.startDateObj = startDateObj;
  }

  public String getRoom() {
    return room;
  }

  public String getLocationDisplayName() {
    return locationDisplayName;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public String getStartDateDisplay() {
    return startDateDisplay;
  }

  public String getTimeRangeDisplay() {
    return timeRangeDisplay;
  }

  public String getSessionCode() {
    return sessionCode;
  }

  public void setSessionCode(String sessionCode) {
    this.sessionCode = sessionCode;
  }

  public String getSessionStartEndDate() {
    return sessionStartEndDate;
  }

  public void setSessionStartEndDate(String sessionStartEndDate) {
    this.sessionStartEndDate = sessionStartEndDate;
  }

  /**
   * The following method Load the Value from AdditionalInfo Object of CourseSection , based on the
   * Key passed as an Parameter.
   * @param courseSection
   * @param key
   * @return
   */
  private String getCourseSectionAdditionalInfoValue(CourseSection courseSection, String key)
  {
    List<AdditionalInfo> additionalInfoList =courseSection.getSectionAdditionalInfos();
    for(AdditionalInfo additionalInfo:additionalInfoList)
    {
      if (additionalInfo.getKey().equals(key))
         return additionalInfo.getValue();
    }
    return "-";
  }
}
