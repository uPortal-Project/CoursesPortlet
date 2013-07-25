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
 * @author Eric Dalquist
 * @version $Revision$
 * @param <TLK> Type of cache key for the term list
 * @param <CK> Type of cache key for the courses by term list
 */
public interface ICacheableCoursesDao<TLK extends Serializable, CK extends Serializable> {

    /**
     * A cache key for the term list that would be retrieved by this request
     */
    public TLK getTermListKey(PortletRequest request);

    /**
     * A cache key for the courses that would be retrieved by this request
     */
    public CK getCoursesByTermKey(PortletRequest request, String termCode, TermList termList);

    /**
     * Get a term list for the current user
     */
    public TermList getTermList(TLK key);

    /**
     * Get courses for a term for the current user
     */
    public CoursesByTerm getCoursesByTerm(CK key);

}
