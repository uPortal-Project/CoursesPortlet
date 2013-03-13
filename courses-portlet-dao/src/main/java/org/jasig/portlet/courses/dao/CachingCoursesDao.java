package org.jasig.portlet.courses.dao;

import java.io.Serializable;

import javax.portlet.PortletRequest;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.springframework.beans.factory.annotation.Required;

/**
 * Wraps a {@link ICacheableCoursesDao} and presents it as a {@link ICoursesDao} adding caching
 * 
 * @author Eric Dalquist
 */
public final class CachingCoursesDao implements ICoursesDao {
    private ICacheableCoursesDao<Serializable, Serializable> coursesDao;
    private SelfPopulatingCache termListCache;
    private SelfPopulatingCache coursesByTermCache;

    /**
     * @param termListCache The cache to use for term lists
     */
    @Required
    public void setTermListCache(Ehcache termListCache) {
        this.termListCache = new SelfPopulatingCache(termListCache, new CacheEntryFactory() {
            @Override
            public Object createEntry(Object key) throws Exception {
                return coursesDao.getTermList((Serializable)key);
            }
        });
    }
    
    /**
     * @param coursesByTermCache The cache to use for courses by term
     */
    @Required
    public void setCoursesByTermCache(Ehcache coursesByTermCache) {
        this.coursesByTermCache = new SelfPopulatingCache(coursesByTermCache, new CacheEntryFactory() {
            @Override
            public Object createEntry(Object key) throws Exception {
                return coursesDao.getCoursesByTerm((Serializable)key);
            }
        });
    }
    
    @Override
    public TermList getTermList(PortletRequest request) {
        final Serializable termListKey = coursesDao.getTermListKey(request);
        
        final Element element = this.termListCache.get(termListKey);
        if (element == null) { 
            return null;
        }
        
        return (TermList)element.getObjectValue();
    }

    @Override
    public CoursesByTerm getCoursesByTerm(PortletRequest request, String termCode) {
        final Serializable coursesByTermKey = coursesDao.getCoursesByTermKey(request, termCode);

        final Element element = this.coursesByTermCache.get(coursesByTermKey);
        if (element == null) { 
            return null;
        }
        
        return (CoursesByTerm)element.getObjectValue();
    }
}
