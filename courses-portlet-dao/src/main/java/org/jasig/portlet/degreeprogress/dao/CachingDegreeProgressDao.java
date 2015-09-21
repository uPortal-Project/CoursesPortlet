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
package org.jasig.portlet.degreeprogress.dao;

import javax.portlet.PortletRequest;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.degreeprogress.model.xml.DegreeProgressReport;

/**
 * This {@link org.jasig.portlet.degreeprogress.dao.IDegreeProgressDao} implementation decorates one or more other DAOs and
 * adds caching features to them.  Whatever data the underlying DAOs provide
 * will be stored in cache according to parameters specified in ehcache.xml.
 * All caching is per-user.
 *
 * @author Chris Waymire, chris@waymire.net
 */
public class CachingDegreeProgressDao implements IDegreeProgressDao {
    private final Log log = LogFactory.getLog(getClass());

    /*
     * Spring-wired dependencies
     */

    private IDegreeProgressDao enclosedDegreeProgressDao;

    public void setEnclosedDegreeProgressDao(IDegreeProgressDao enclosedDegreeProgressDao) {
        this.enclosedDegreeProgressDao = enclosedDegreeProgressDao;
    }

    private Cache progressReportCache;

    public void setProgressReportCache(Cache progressReportCache) {
        this.progressReportCache = progressReportCache;
    }

    @Override
    public DegreeProgressReport getProgressReport(PortletRequest request) {
        DegreeProgressReport rslt =  null;
        String cacheKey = getDegreeProgressCacheKey(request);
        Element m = progressReportCache.get(cacheKey);

        if (m != null) {
            rslt = (DegreeProgressReport) m.getValue();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Fetching new DegreeProgressReport from enclosedDegreeProgressDao for user '" + request.getRemoteUser() + "'");
            }
            rslt = enclosedDegreeProgressDao.getProgressReport(request);
            m = new Element(cacheKey, rslt);
            progressReportCache.put(m);
        }
        return rslt;
    }

    @Override
    public Boolean getWebEnabled(PortletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WhatIfRequest createWhatIfRequest(PortletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DegreeProgressReport getWhatIfReport(WhatIfRequest whatIfRequest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getDegreeProgressCacheKey(PortletRequest request) {
        return CachingDegreeProgressDao.class.getName() + "." + request.getRemoteUser();

    }
}
