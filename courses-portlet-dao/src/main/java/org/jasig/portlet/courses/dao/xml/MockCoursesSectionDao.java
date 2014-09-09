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
package org.jasig.portlet.courses.dao.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.dao.ICoursesSectionDao;
import org.jasig.portlet.courses.model.xml.CourseMeeting;
import org.jasig.portlet.courses.model.xml.CourseSection;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.Course;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.model.xml.personal.TermsAndCourses;
import org.jasig.portlet.courses.util.CourseCompareByDeptAndCatalog;
import org.springframework.core.io.Resource;

import javax.portlet.PortletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: saikatsengupta
 * Date: 10/15/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockCoursesSectionDao implements ICoursesSectionDao {

    protected final Log log = LogFactory.getLog(getClass());
    private TermsAndCourses summary;
    private Resource mockData;

    public void setMockData(Resource mockData) {
        this.mockData = mockData;
    }


    @Override
    public TermList getTermList(PortletRequest request) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());
        } catch (IOException e) {
            log.error("Failed to read mock data for TermList", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data for TermList", e);
        }
        return summary.getTermList();
    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode, TermList termList) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());


        } catch (IOException e) {
            log.error("Failed to read mock data for CourseByTerm", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data for CourseByTerm", e);
        }
        CoursesByTerm courseByTerm=this.summary.getCoursesByTerm(termCode);
        final List<Course> courses = courseByTerm.getCourses();
        //sort courses
        Collections.sort(courses, new CourseCompareByDeptAndCatalog());
        return courseByTerm;
    }

    @Override
    public Course getCoursesBySection(PortletRequest request, String termCode, String catalogNbr, String subjectCode, String courseId, String classNbr, TermList termList) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());


        } catch (IOException e) {
            log.error("Failed to read mock data for CourseByTerm", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data for CourseByTerm", e);
        }
        CoursesByTerm courseByTerm= this.summary.getCoursesByTerm(termCode);
        for (Course course :courseByTerm.getCourses())
        {
            if (course.getId().equals(courseId))
            {
                log.debug("Course Found !!!!");
                return course;
            }
        }
        return null;
    }

    @Override
    public List<Course> getFinalExams(PortletRequest request, String termCode, TermList termList) {
        List<Course> courseList=new ArrayList<Course>();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());
            CoursesByTerm courseByTerm= this.summary.getCoursesByTerm(termCode);
            courseList=courseByTerm.getCourses();
            for (Course course:courseList)
            {
                for (CourseSection courseSection:course.getCourseSections())
                {
                    List<CourseMeeting> courseMeetings  =new ArrayList<CourseMeeting>();
                    for (CourseMeeting courseMeeting:courseSection.getCourseMeetings())
                    {
                        if (courseMeeting.getType().equals("EXAM"))
                        {
                            courseMeetings.add(courseMeeting);
                        }
                    }
                    courseSection.getCourseMeetings().clear();
                    courseSection.getCourseMeetings().addAll(courseMeetings);
                }
            }


        } catch (IOException e) {
            log.error("Failed to read mock data for CourseByTerm", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data for CourseByTerm", e);
        }
        return courseList;

    }

    @Override
    public List<Course> getClassSchedule(PortletRequest request, String termCode, TermList termList) {
        List<Course> courseList=new ArrayList<Course>();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TermsAndCourses.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.summary = (TermsAndCourses) unmarshaller.unmarshal(mockData.getInputStream());
            CoursesByTerm courseByTerm= this.summary.getCoursesByTerm(termCode);
            courseList=courseByTerm.getCourses();
            for (Course course:courseList)
            {
                for (CourseSection courseSection:course.getCourseSections())
                {
                    List<CourseMeeting> courseMeetings  =new ArrayList<CourseMeeting>();
                    for (CourseMeeting courseMeeting:courseSection.getCourseMeetings())
                    {
                        if (courseMeeting.getType().equals("CLASS"))
                        {
                            courseMeetings.add(courseMeeting);
                        }
                    }
                    courseSection.getCourseMeetings().clear();
                    courseSection.getCourseMeetings().addAll(courseMeetings);
                }
            }


        } catch (IOException e) {
            log.error("Failed to read mock data for CourseByTerm", e);
        } catch (JAXBException e) {
            log.error("Failed to unmarshall mock data for CourseByTerm", e);
        }
        return courseList;
    }
}
