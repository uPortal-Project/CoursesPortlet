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

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;

/**
 * This {@link ICoursesDao} implementation decorates one or more other DAOs and 
 * adds caching features to them.  Whatever data the underlying DAOs provide 
 * will be stored in cache according to parameters specified in ehcache.xml.  
 * All caching is per-user.
 * 
 * @author awills
 */
public final class CachingCoursesDao implements ICoursesDao {
    
    private static final String UNAUTHENTICATED_USER_TERMLIST_CACHE_KEY = "CachingCoursesDao.UNAUTHENTICATED_USER_TERMLIST_CACHE_KEY";
    
    private final Log log = LogFactory.getLog(getClass());

    /*
     * Spring-wired dependencies
     */
    
    private ICoursesDao enclosedCoursesDao;
    
    public void setEnclosedCoursesDao(ICoursesDao enclosedCoursesDao) {
        this.enclosedCoursesDao = enclosedCoursesDao;
    }

    private Cache termListCache;
    
    public void setTermListCache(Cache termListCache) {
        this.termListCache = termListCache;
    }
    
    private Cache coursesByTermCache;
    
    public void setCoursesByTermCache(Cache coursesByTermCache) {
        this.coursesByTermCache = coursesByTermCache;
    }

    /*
     * Public API
     */

    @Override
    public TermList getTermList(PortletRequest req) {
        
        TermList rslt = null;

        final String cacheKey = getTermListCacheKey(req);
        Element m = termListCache.get(cacheKey);
        
        if (m != null) {
            rslt = (TermList) m.getValue();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Fetching new TermList from enclosedCoursesDao for user '" + req.getRemoteUser() + "'");
            }
            rslt = enclosedCoursesDao.getTermList(req);
            m = new Element(cacheKey, rslt);
            termListCache.put(m);
        }
        
        return rslt;

    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest req, String termCode) {

        CoursesByTerm rslt = null;
    
        final String cacheKey = getCoursesByTermCacheKey(req, termCode);
        if (cacheKey == null) {
            // Unauthenticated user;  save the enclosedCoursesDao the bother...
            rslt = new CoursesByTerm();
            rslt.setTermCode(termCode);
            return rslt;
        }

        Element m = coursesByTermCache.get(cacheKey);
        if (m != null) {
            rslt = (CoursesByTerm) m.getValue();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Fetching new CoursesByTerm from enclosedCoursesDao for user '" + req.getRemoteUser() + "'");
            }
            rslt = enclosedCoursesDao.getCoursesByTerm(req, termCode);
            m = new Element(cacheKey, rslt);
            coursesByTermCache.put(m);
        }
    
        return rslt;

    }
    
    /*
     * Implementation
     */
    
    /**
     * Provides the appropriate {@link TermList} cache key for either inserting 
     * or retrieving.  <strong>NOTE:</strong>  This method provides a key even 
     * for unauthenticated users, since some DAOs may be able to provide a 
     * TermList and some future feature of this portlet might be able to provide 
     * guest content.
     */
    private String getTermListCacheKey(PortletRequest req) {
        final String rslt = req.getRemoteUser() != null 
                ? req.getRemoteUser() 
                : UNAUTHENTICATED_USER_TERMLIST_CACHE_KEY;
        return rslt;
    }

    /**
     * Provides the appropriate {@link CoursesByTerm} cache key for either 
     * inserting or retrieving, or null if the user is not authenticated. 
     */
    private String getCoursesByTermCacheKey(PortletRequest req, String termCode) {
        final String username = req.getRemoteUser();
        if (username != null) {
            return username.concat("|").concat(termCode);
        }
        return null;  // fall-back
    }

}
