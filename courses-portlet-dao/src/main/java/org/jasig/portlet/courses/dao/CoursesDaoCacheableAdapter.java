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
