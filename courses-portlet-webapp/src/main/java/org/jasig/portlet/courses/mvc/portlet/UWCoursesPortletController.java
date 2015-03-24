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
package org.jasig.portlet.courses.mvc.portlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jasig.portlet.courses.dao.ICoursesSectionDao;
import org.jasig.portlet.courses.model.xml.*;
import org.jasig.portlet.courses.model.xml.personal.Course;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.mvc.wrapper.CourseSectionMeetingWrapper;
import org.jasig.portlet.courses.mvc.wrapper.CourseSectionWrapper;
import org.jasig.portlet.courses.service.IURLService;
import org.jasig.portlet.utils.mvc.IViewSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;

import java.io.IOException;
import java.util.*;
@Controller
@RequestMapping("VIEW")

public class UWCoursesPortletController{
    public static final String DEFAULT_VIEW_PREFERENCE = "defaultView";
    public static final String COURSE_LIST_VIEW = "courseList";
    public static final String CLASS_SCHEDULE_VIEW = "classSchedule";
    public static final String GRADES_LIST_VIEW = "grades";
    public static final String TERMCODE = "termCode";
    public static final String VIEW = "view";
    public static final String PRINTVIEW = "printView";
    public static final String COURSECODE = "courseCode";
    public static final String CATALOGNBR = "catalogNbr";
    public static final String SUBJECTCODE = "subjectCode";
    public static final String CLASSNBR = "classNbr";
    public static final String COURSEID = "courseId";
    public static final String STANDALONE_SCHEDULE_PREFERENCE = "standaloneSchedule";
    private static final String DISPLAY_COURSE_UPDATES_PREFERENCE = "displayCourseUpdates";
    private static final String DISPLAY_COURSE_BOOKS_PREFERENCE = "displayCourseBooks";
    public static final String FINALGRADESFNAME_PREF = "finalGradesFname";
    public static final String FINALGRADESWINDOWSTATE_PREF = "finalGradesWindowState";
    public static final String FINALGRADESFIXEDPARAM_PREF = "finalGradesWindowFixedParameterString";



  private ICoursesSectionDao coursesSectionDao;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("coursesSectionDao")
    public void setCoursesSectionDao(ICoursesSectionDao coursesSectionDao) {
        this.coursesSectionDao = coursesSectionDao;
    }


  /*@Autowired
    @Qualifier("finalGradesServiceDao")
    public void setCoursesDao(ICoursesDao coursesDao) {
        this.coursesDao = coursesDao;
    }
    */
    private IURLService urlService;

    @Autowired
    public void setUrlService(IURLService urlService) {
        this.urlService = urlService;
    }

    private IViewSelector viewSelector;

    @Autowired
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }

    @RequestMapping
    public String defaultView(PortletRequest request, MimeResponse mimeResponse,ModelMap model) {
        final PortletPreferences preferences = request.getPreferences();
        final String defaultView = preferences.getValue(DEFAULT_VIEW_PREFERENCE, COURSE_LIST_VIEW);

        logger.info("Calling defaultView.....");
        if (CLASS_SCHEDULE_VIEW.equals(defaultView)) {
            request.getPortletSession().setAttribute("helpDeskURL",preferences.getValue("helpDeskURL", "https://kb.wisc.edu/helpdesk/page.php?id=35501"));
            return viewClassSchedule(request, mimeResponse,model, null,"grid",false);
        }
        request.getPortletSession().setAttribute("helpDeskURL",preferences.getValue("helpDeskURL", "https://kb.wisc.edu/helpdesk/page.php?id=35501"));
        return viewCourseList(request, model, null);
    }

    @RequestMapping(params = "action=" + COURSE_LIST_VIEW)
    public String viewCourseList(PortletRequest request, ModelMap model,
            @RequestParam(value = TERMCODE, required = false) String termCode) {

        TermList termList = coursesSectionDao.getTermList(request);
        model.put("termList", termList);
        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
        //Grab the coursesByTerm information if a selectedTerm is set
        if (selectedTerm != null) {
            final CoursesByTerm coursesByTerm = coursesSectionDao.getCoursesByTerm(request, selectedTerm.getCode(),termList);

            model.put("coursesByTerm", coursesByTerm);
            model.put("selectedTerm", selectedTerm);
        }
        final boolean isMobile = viewSelector.isMobile(request);
        model.put("isMobile", isMobile);
        final String viewName = isMobile ? "myuwcourses/courseList-jQM" : "myuwcourses/courseList";
        return viewName;
    }


    @RequestMapping(params = "action=showCourse")
    public String viewCourse(PortletRequest request, ModelMap model,
            @RequestParam(TERMCODE) String termCode,
            @RequestParam(CATALOGNBR) String catalogNbr,
            @RequestParam(SUBJECTCODE) String subjectCode,
            @RequestParam(CLASSNBR) String classNbr,
            @RequestParam(COURSEID) String courseId)
    {
        final boolean isMobile = viewSelector.isMobile(request);
        PortletPreferences prefs = request.getPreferences();
        TermList termList = coursesSectionDao.getTermList(request);

        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
        model.put("selectedTerm", selectedTerm);

        CoursesByTerm summary = coursesSectionDao.getCoursesByTerm(request, termCode, termList);
        model.put("coursesByTerm", summary);
        logger.debug("selectedCourse code is ....."+catalogNbr+subjectCode);
        Course course= coursesSectionDao.
                                      getCoursesBySection(request,termCode, catalogNbr,
                                                          subjectCode,courseId,classNbr,termList);
            Map<String, String> instructorUrls = new HashMap<String, String>();
            for (final CourseSection courseSection: course.getCourseSections())
            {
              //Instructors
              for (final Instructor instructor : courseSection.getInstructors()) {
                logger.debug("instructor.getIdentifier()........" + instructor.getIdentifier());
                logger.debug("instructor.getFullName()......." + instructor.getFullName());
                logger.debug("instructor.getAbbreviation()........." + instructor.getAbbreviation());
                logger.debug("instructor.getInstructorAdditionalInfos()........." + instructor.getInstructorAdditionalInfos());
                instructorUrls.put(instructor.getIdentifier(), urlService.getInstructorUrl(instructor, request));
                if (logger.isDebugEnabled()) {
                  logger.debug("instructorUrls......." + instructorUrls);
                }
              }
            }
            //Setting finalGrades URL
            Map<String,String> params=new HashMap<String,String>();
            params.put("pP_termCode",termCode);
            String fgBaseUrl = urlService.getOtherPortletURL(prefs,
                                                           FINALGRADESFNAME_PREF,
                                                           FINALGRADESWINDOWSTATE_PREF,
                                                           FINALGRADESFIXEDPARAM_PREF,
                                                           params);
            model.put("finalGradeUrl", fgBaseUrl);
            model.put("instructorUrls", instructorUrls);
            model.put("courseSectionMeetingList",getCourseMeetingsWrapperList(course,prefs,isMobile,termCode));
            model.put("course", course);
        final String viewName = isMobile ? "myuwcourses/courseDetail-jQM" : "myuwcourses/courseDetail";
        return viewName;
    }

    @RequestMapping(params = "action=showExams")
    public String viewExam(PortletRequest request,
                           MimeResponse response,
                           ModelMap model,
                           @RequestParam(TERMCODE) String termCode,
                           @RequestParam(VIEW) String view,
                           @RequestParam(value = PRINTVIEW, required = false, defaultValue = "false") Boolean printView)
    {
        final TermList termList = coursesSectionDao.getTermList(request);
        model.put("termList", termList);

        model.put(PRINTVIEW, printView);

        model.put("isStandaloneSchedule",
                  Boolean.parseBoolean(request.getPreferences().getValue(STANDALONE_SCHEDULE_PREFERENCE, "false")));

        //Determine the current term code and term
        termCode = this.getSelectedTermCode(request, termCode);
        final Term selectedTerm = this.getSelectedTerm(termCode, termList);
        //Grab the coursesByTerm information if a selectedTerm is set
        if (selectedTerm != null) {
            final CoursesByTerm coursesByTerm = coursesSectionDao.getCoursesByTerm(request, selectedTerm.getCode(),termList);
            model.put("coursesByTerm", coursesByTerm);
            model.put("selectedTerm", selectedTerm);
        }
        logger.debug("Calling getFinalExams....."+termCode);
        List<Course> courseList= coursesSectionDao.getFinalExams(request, termCode,termList);
        List<CourseSectionWrapper> finalExamsList=new ArrayList<CourseSectionWrapper>();
        for (Course course : courseList)
        {
          for (CourseSection courseSection: course.getCourseSections())
          {
            PortletURL url = createCourseDetailUrl(course, courseSection, response, termCode);

            for (CourseMeeting courseMeeting: courseSection.getCourseMeetings())
            {
              CourseSectionWrapper finalExam = new CourseSectionWrapper(
                course,
                courseSection,
                courseMeeting,
                url.toString()
              );
              finalExamsList.add(finalExam);
            }
          }
        }

        Collections.sort(finalExamsList, CourseSectionWrapper.FinalExamComparator);

        model.put("finalExamsList", finalExamsList);

        if( finalExamsList.isEmpty() ) {
          model.put("startDateSunday", false);
        } else {
          model.put("startMonthDescr", finalExamsList.get(0).getMonthDescrOfStart());
          model.put("startDateSunday", finalExamsList.get(0).getExamStartSunday());
        }

        final boolean isMobile = viewSelector.isMobile(request);
        String viewName="";
        if (view.equals("list")) {
            viewName = isMobile ? "myuwcourses/examList-jQM" : "myuwcourses/examList";
        } else {
          ObjectMapper mapper = new ObjectMapper();
          try {
            model.put("finalExamsListJson",
              mapper.writeValueAsString((List<CourseSectionWrapper>)model.get("finalExamsList")));
          } catch (IOException e) {
            String msg = "Failed to return jsonClassSchedule response";
            logger.error(msg, e);
            throw new RuntimeException(msg);
          }

          viewName = isMobile ? "myuwcourses/examGrid-jQM" : "myuwcourses/examGrid";
        }

        return viewName;
    }

    /**
    * Sets "otherCourses" and "classScheduleList" on the model
    * <p/>
    * Both are a List<{@link CourseSectionWrapper}>, defining the lists of classes which are to be displayed
    *
    * @param request  The {@link PortletRequest}
    * @param response Required for generating {@link PortletURL}s for the front end
    * @param model    The model
    * @param termCode Required for getting the list of courses and generating URLs
    */
    private void getClassScheduleList(PortletRequest request,
                                      MimeResponse response,
                                      ModelMap model,
                                      String termCode)
    {

      final TermList termList = coursesSectionDao.getTermList(request);
      model.put("termList", termList);

      //Determine the current term code and term
      termCode = this.getSelectedTermCode(request, termCode);
      final Term selectedTerm = this.getSelectedTerm(termCode, termList);
      //Grab the coursesByTerm information if a selectedTerm is set
      if (selectedTerm == null)   return;
        logger.info("FOUND SELECTED TERM !!!!"+selectedTerm.getCode());
        final CoursesByTerm coursesByTerm = coursesSectionDao.getCoursesByTerm(request, selectedTerm.getCode(),termList);
        model.put("coursesByTerm", coursesByTerm);
        model.put("selectedTerm", selectedTerm);

      List<Course> courseList= coursesSectionDao.getClassSchedule(request, selectedTerm.getCode(), termList);
      List<CourseSectionWrapper> classScheduleList=new ArrayList<CourseSectionWrapper>();
      List<CourseSectionWrapper> otherCourses=new ArrayList<CourseSectionWrapper>();
      for (Course course : courseList)
      {
        for (CourseSection courseSection: course.getCourseSections())
        {
          PortletURL url = createCourseDetailUrl(course, courseSection, response, selectedTerm.getCode());

          if( courseSection.getCourseMeetings().size() > 0
              && !courseSection.getCourseMeetings().get(0).getLocation().getDisplayName().equals("ONLINE"))
          {
            for (CourseMeeting courseMeeting: courseSection.getCourseMeetings())
            {
              if (courseMeeting.getDayIds()!=null)
              {
                for (String day : courseMeeting.getDayIds())
                {
                  CourseSectionWrapper classSchedule =
                    new CourseSectionWrapper(course, courseSection, courseMeeting, day, url.toString());
                  classScheduleList.add(classSchedule);
                }
              }
            }
          } else {
            CourseSectionWrapper classSchedule =
              new CourseSectionWrapper(course, courseSection, url.toString());
            otherCourses.add(classSchedule);
          }
        }
      }

      Collections.sort(classScheduleList, CourseSectionWrapper.ClassScheduleComparator);
      Collections.sort(otherCourses,CourseSectionWrapper.OtherCourseComparator);
      model.put("otherCourses", otherCourses);
      model.put("classScheduleList", classScheduleList);
    }

    @ResourceMapping("jsonClassSchedule")
    public String jsonClassSchedule(PortletRequest request, MimeResponse response, ModelMap model,
                                    @RequestParam(TERMCODE) String termCode)
    {
      getClassScheduleList(request, response, model, termCode);
      return "json";
    }
    
    @ResourceMapping("jsonCurrentClassSchedule")
    public String jsonCurrentClassSchedule(PortletRequest request, MimeResponse response, ModelMap model)
    {
      final TermList termList = coursesSectionDao.getTermList(request);
      if(termList != null) {
        model.put("termList", termList);
        if(termList.getCurrentTerm() != null) {
          final CoursesByTerm coursesByTerm = coursesSectionDao.getCoursesByTerm(request, termList.getCurrentTerm().getCode(), termList);
          model.put("coursesByTerm", coursesByTerm);
        } else {
          model.put("coursesByTerm", null);
        }
      }
      
      return "json";
      
    }

    @RequestMapping(params = "action=showClassSchedule")
    public String viewClassSchedule(PortletRequest request, MimeResponse response, ModelMap model,
                                    @RequestParam(TERMCODE) String termCode,
                                    @RequestParam(VIEW) String view,
                                    @RequestParam(value = PRINTVIEW, required = false, defaultValue = "false") Boolean printView)
    {
      getClassScheduleList(request, response, model, termCode);

      final boolean isMobile = viewSelector.isMobile(request);
      String viewName="";

      model.put(PRINTVIEW, printView);

      model.put("isStandaloneSchedule",
                Boolean.parseBoolean(request.getPreferences().getValue(STANDALONE_SCHEDULE_PREFERENCE, "false")));

      if (view.equals("list")) {
        viewName = isMobile ? "myuwcourses/classScheduleList-jQM" : "myuwcourses/classScheduleList";
      } else {
        ObjectMapper mapper = new ObjectMapper();
        try {
          model.put("classScheduleListJson",
                    mapper.writeValueAsString((List<CourseSectionWrapper>)model.get("classScheduleList")));
        } catch (IOException e) {
          String msg = "Failed to return jsonClassSchedule response";
          logger.error(msg, e);
          throw new RuntimeException(msg);
        }
        viewName = isMobile ? "myuwcourses/classScheduleGrid-jQM" : "myuwcourses/classScheduleGrid";
      }
      return viewName;
    }

    /**
     * Action request handler that simply copies all parameters from action to render
     */
    @ActionMapping
    public void copyActionParameters(ActionRequest actionRequest, ActionResponse actionResponse) {
        actionResponse.setRenderParameters(actionRequest.getParameterMap());
    }

    @ModelAttribute("displayCourseUpdates")
    public Boolean getDisplayCourseUpdates(PortletRequest req) {
        String val = req.getPreferences().getValue(DISPLAY_COURSE_UPDATES_PREFERENCE,
                Boolean.TRUE.toString());
        return Boolean.valueOf(val);
    }

    @ModelAttribute("displayCourseBooks")
    public Boolean getDisplayCourseBooks(PortletRequest req) {
        String val = req.getPreferences().getValue(DISPLAY_COURSE_BOOKS_PREFERENCE,
                Boolean.TRUE.toString());
        return Boolean.valueOf(val);
    }

    /**
     * Determine the term code to use. If a term code is specified on the request it is returned and stored
     * in the portlet session. If no term code is specified on the request the session is checked for a stored
     * term code.
     */
    protected String getSelectedTermCode(PortletRequest portletRequest, String requestTermCode) {
        final PortletSession portletSession = portletRequest.getPortletSession();
        if (requestTermCode != null) {
            portletSession.setAttribute(TERMCODE, requestTermCode);
            return requestTermCode;
        }
        return (String)portletSession.getAttribute(TERMCODE);
    }

    /**
     * If termCode is null {@link TermList#getCurrentTerm()} is used, if not {@link TermList#getTerm(String)} is used
     */
    protected Term getSelectedTerm(String termCode, final TermList termList) {
        if (termCode == null) {
            return termList.getCurrentTerm();
        }
        return termList.getTerm(termCode);
    }

    private String getPVI(List<AdditionalInfo> addInfoList) {
      for (AdditionalInfo addInfo : addInfoList) {
        if("pvi".equals(addInfo.getKey()))
          return addInfo.getValue();
      }
      return null;
    }


    /**
     * Generate a link to the Course Detail View for the given course parameters
     * <p/>
     * These are subject to change, as <pre>courseCode</pre> isn't necessary and
     * <pre>classNbr</pre> shouldn't be necessary.
     *
     * @param course   The course to get the detail view link for
     * @param section  Provides the <pre>courseId</pre> parameter.
     *                 Should be removed.
     * @param response Required to create the {@link PortletURL} object
     * @param term     Term code for the <pre>termCode</pre> url parameter
     *                 (obviously)
     *
     * @return A PortletURL with all the parameters defined to link to the students
     *         course detail page for the specified course and term
     */
    private PortletURL createCourseDetailUrl(Course course, CourseSection section, MimeResponse response, String term) {
      PortletURL url = response.createRenderURL();
      url.setParameter("classNbr", section.getId());
      url.setParameter("action", "showCourse");
      url.setParameter("courseId", course.getId());
      url.setParameter("subjectCode", course.getCourseDepartment().getCode());
      url.setParameter("termCode", term);
      url.setParameter("catalogNbr", course.getCode());
      url.setParameter("courseCode", course.getCode());

      return url;
    }

  private List<CourseSectionMeetingWrapper> getCourseMeetingsWrapperList(Course course,PortletPreferences prefs,boolean isMobile,String termCode)
  {
    List<CourseSectionMeetingWrapper> courseSectionMeetingWrapperList =new ArrayList<CourseSectionMeetingWrapper>();
    for (CourseSection courseSection:course.getCourseSections())
    {
      logger.debug("DUMPING COURSE MEETINGS >>>>>>>>>>>"+courseSection.getCode());
      logger.debug("DUMPING COURSE MEETINGS SIZE>>>>>>>>>>>"+courseSection.getCourseMeetings().size());
      courseSectionMeetingWrapperList.add(new CourseSectionMeetingWrapper(courseSection,prefs,isMobile,termCode,urlService));
    }

    if (logger.isDebugEnabled())
    {
      logger.debug("DUMPING COURSE MEETINGS>>>>>>>>>>>");
      new CourseSectionMeetingWrapper().printCourseSectionWrapperList(courseSectionMeetingWrapperList);
    }
      return courseSectionMeetingWrapperList;
  }


}
