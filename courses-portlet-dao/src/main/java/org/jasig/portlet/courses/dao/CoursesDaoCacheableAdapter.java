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

import java.io.Serializable;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;

/**
 * Adapts any {@link ICacheableCoursesDao} to a {@link ICoursesDao}
 * 
 * @author Eric Dalquist
 */
public final class CoursesDaoCacheableAdapter implements ICoursesDao {
    private final ICacheableCoursesDao<Serializable, Serializable> cacheableCoursesDao;
    
    public CoursesDaoCacheableAdapter(ICacheableCoursesDao<Serializable, Serializable> cacheableCoursesDao) {
        this.cacheableCoursesDao = cacheableCoursesDao;
    }

    @Override
    public TermList getTermList(PortletRequest request) {
        final Serializable termListKey = this.cacheableCoursesDao.getTermListKey(request);
        return this.cacheableCoursesDao.getTermList(termListKey);
    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode) {
        final TermList termList = this.getTermList(request);
        final Serializable coursesByTermKey = this.cacheableCoursesDao.getCoursesByTermKey(request, termCode, termList);
        return this.cacheableCoursesDao.getCoursesByTerm(coursesByTermKey);
    }
}
