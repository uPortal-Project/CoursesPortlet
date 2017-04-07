# Courses Portlet

[![Linux Build Status](https://travis-ci.org/Jasig/CoursesPortlet.svg?branch=master)](https://travis-ci.org/Jasig/CoursesPortlet)
[![Windows Build status](https://ci.appveyor.com/api/projects/status/nqxqteabp0lqhfl6/branch/master?svg=true)](https://ci.appveyor.com/project/ChristianMurphy/coursesportlet/branch/master)

## Table of Contents

*   [Description](#description)
*   [Features](#features)
*   [Configuration and Installation](#configuration-and-installation)
    *   [Sakai](#sakai)
    *   [Moodle](#moodle)
    *   [Blackboard](#blackboard)
*   [Integrating with Your SIS or LMS](#integrating-with-your-sis-or-lms)
*   [Where to get help](#where-to-get-help)
*   [Contribution Guidelines](#contribution-guidelines)
*   [Roadmap](#roadmap)
    *   [Grade Reporting](#grade-reporting)
    *   [GPA Calculation](#gpa-calculation)
    *   [Share data with other portlets](#share-data-with-other-portlets)
*   [License](#license)

## Description


The Courses Portlet is a JSR-286 read-only LMS integration portlet.

## Features

*   Display a list of courses for the viewing user's current semester
*   Display critical details for a course, including instructor, meeting time, and location
*   Display recent updates from the course, including items like announcements and forum posts
*   Integration with Calendar Portlet

## Configuration and Installation

### Sakai

A Jasig courses plugin is available for Sakai at <https://source.sakaiproject.org/contrib/jasig-courses-integration/>.  This plugin outputs XML representing the user's courses in the format expected by the Courses Portlet. This plugin is not currently capable of limiting results by semester.

### Moodle

A Moodle plugin is in-progress.

### Blackboard

While no Blackboard plugin currently exists, the project would welcome a contribution from a Blackboard institution.

## Integrating with Your SIS or LMS

Data sources for portlets in the Courses Portlet project leverage a common paradigm in Apereo portlet development – a pluggable, interface-based abstraction.  The interface for the contained portlet named 'Courses' (for example) is `ICoursesDao`.
You can integrate the 'Courses' portlet with any SIS and/or LMS by providing a custom implementation of this Java interface and wiring it appropriately in the *applicationContext.xml* Spring configuration file for the webapp.  Define a Spring `<bean>` based on your implementation class and add it as a child of the 'courseDaos' (list) bean.  A couple examples are provided in the file to illustrate the process.

``` Java
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

package org.jasig.portlet.courses.dao;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;

/**
 * ICoursesDao represents a data access interface for retrieving course and
 * grade data for a particular user.
 *
 * @author Jen Bourey, jennifer.bourey@gmail.com
 * @version $Revision$
 */
public interface ICoursesDao {

    /**
     * Get a term list for the current user
     *
     * @param request
     * @return
     */
    public TermList getTermList(PortletRequest request);

    /**
     * Get courses for a term for the current user
     *
     * @param request
     * @return
     */
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode);

}
```

## Where to get help

The <uportal-user@apereo.org> mailing list is the best place to go with
questions related to Apereo portlets and uPortal.

Issues should be reported at <https://issues.jasig.org/browse/COURSESPLT>.
Check if your issue has already been reported. If so, comment that you are also
experiencing the issue and add any detail that could help resolve it. Feel free to
create an issue if it has not been reported. Creating an account is free and can be
initiated at the Login widget in the default dashboard.

## Contribution Guidelines
Apereo requires contributors sign a contributor license agreement (CLA).
We realize this is a hurdle. To learn why we require CLAs, see
"Q5. Why does Apereo require Contributor License Agreements (CLAs)?"
at <https://www.apereo.org/licensing>.

The CLA form(s) can be found <https://www.apereo.org/licensing/agreements> along
with the various ways to submit the form.

Contributions will be accepted once the contributor's name appears at
<http://licensing.apereo.org/completed-clas>.

See <https://www.apereo.org/licensing> for details.


## Roadmap

### Grade Reporting

Apereo would like to add a dedicated grade reporting view to the courses portlet.  While the current portlet can display a grade in each course's detail view, it would be useful to have an overview with the following features:

*   Grades and credits for each course in a term
*   Term GPA and credit total
*   Ability to navigate between terms for which the user has completed courses
*   Overal GPA and credit total

In the initial phase of this feature, term and overall GPA and credit number will be provided by the remote service and will not be calculated by the portlet itself.

This use case will require adding data representations of academic terms, GPA, and course credits to the portlet, as well as adding new views.

### GPA Calculation

In the longer term, some institutions may want the portlet to be capable of calculating GPAs itself.  This would help support use cases where a student could enter hypothetical grades for current courses to get a preview of the resulting GPA.

An in-portlet GPA calculator would need to take into account a number of institution-specific rules.  Different schools use slightly different numerical values for GPA fractions (for instance, mapping an "A-" to a 3.67 vs. 3.667 or 3.66).  Other schools do not use letter grades at all, instead assigning numerical grades directly.  Institutions may also vary in their treatment of re-taken classes.

Eventually the portlet may also need to distinguish between different enrollment periods and degrees while calculating overall GPA (e.g. undergraduate degree vs. medical school).

This functionality should be optional, as some schools might wish to continue to allow the external system to calculate a student's GPA.

### Share data with other portlets

Jasig would like to have course-related information available to other portlets.  Some examples where this would be useful are

1.  Calendar portlet that can integrate a student's or teacher's course schedule into their calendar
2.  Map view that obtains the current list of courses and their locations to display a visual map to the student.

To accomplish this we'd like to have the courses portlet DAO code split into a separate module so it can be included in other portlets to access course-related information. [COURSESPLT-10](https://issues.jasig.org/browse/COURSESPLT-10)

## License

Copyright 2016 Apereo Foundation, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this project except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

See <https://www.apereo.org/licensing> for additional details.
